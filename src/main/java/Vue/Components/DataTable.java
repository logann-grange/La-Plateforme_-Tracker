package Vue.Components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class DataTable {
    private static final double TABLE_ROW_HEIGHT = 36;
    private static final double TABLE_HEADER_HEIGHT = 30;
    private static final double TABLE_VERTICAL_BUFFER = 24;

    private final TableView<StudentRow> tableView;
    private final VBox root;
    private final int defaultPageSize;

    private List<StudentRow> sourceRows;
    private List<StudentRow> allRows;
    private List<String> activeFilters;
    private String currentQuery;
    private String currentIdFilter;
    private String currentAgeFilter;
    private String currentAverageFilter;
    private String currentSortBy;
    private String currentSortOrder;
    private int currentPage;
    private int pageSize;

    private Consumer<StudentRow> onEdit;
    private Consumer<StudentRow> onDelete;
    private Consumer<StudentRow> onGradeClick;

    public DataTable() {
        this(10);
    }

    public DataTable(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize doit etre strictement positif.");
        }

        this.defaultPageSize = pageSize;
        this.pageSize = pageSize;
        this.currentPage = 1;
        this.sourceRows = new ArrayList<>();
        this.allRows = new ArrayList<>();
        this.activeFilters = new ArrayList<>();
        this.currentQuery = "";
        this.currentIdFilter = "";
        this.currentAgeFilter = "";
        this.currentAverageFilter = "";
        this.currentSortBy = null;
        this.currentSortOrder = null;
        this.onEdit = row -> {
        };
        this.onDelete = row -> {
        };
        this.onGradeClick = row -> {
        };

        this.tableView = new TableView<>();
        this.root = new VBox(12);

        configureTable();
        refreshPage();
    }

    public VBox build() {
        return root;
    }

    public void setRows(List<StudentRow> rows) {
        this.sourceRows = rows == null ? new ArrayList<>() : new ArrayList<>(rows);
        this.currentPage = 1;
        recomputeRows();
    }

    public void setOnEdit(Consumer<StudentRow> onEdit) {
        this.onEdit = onEdit == null ? row -> {
        } : onEdit;
    }

    public void setOnDelete(Consumer<StudentRow> onDelete) {
        this.onDelete = onDelete == null ? row -> {
        } : onDelete;
    }

    public void setOnGradeClick(Consumer<StudentRow> onGradeClick) {
        this.onGradeClick = onGradeClick == null ? row -> {
        } : onGradeClick;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize doit etre strictement positif.");
        }

        this.pageSize = pageSize;
        this.currentPage = 1;
        refreshPage();
    }

    public void resetPageSize() {
        this.pageSize = defaultPageSize;
        this.currentPage = 1;
        refreshPage();
    }

    public TableView<StudentRow> getTableView() {
        return tableView;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<StudentRow> getFilteredRows() {
        return new ArrayList<>(allRows);
    }

    public int getTotalPages() {
        if (allRows.isEmpty()) {
            return 1;
        }
        return (int) Math.ceil((double) allRows.size() / pageSize);
    }

    public void sortRows(String sortBy, String sortOrder) {
        this.currentSortBy = sortBy;
        this.currentSortOrder = sortOrder;
        currentPage = 1;
        recomputeRows();
    }

    public void applyFilterAndSort(List<String> selectedOptions, String sortBy, String sortOrder) {
        this.activeFilters = selectedOptions == null ? new ArrayList<>() : new ArrayList<>(selectedOptions);
        this.currentSortBy = sortBy;
        this.currentSortOrder = sortOrder;
        currentPage = 1;
        recomputeRows();
    }

    public void applyFilters(List<String> selectedOptions) {
        this.activeFilters = selectedOptions == null ? new ArrayList<>() : new ArrayList<>(selectedOptions);
        currentPage = 1;
        recomputeRows();
    }

    public void searchRows(String query) {
        this.currentQuery = query == null ? "" : query.trim().toLowerCase();
        currentPage = 1;
        recomputeRows();
    }

    public void searchById(String idQuery) {
        this.currentIdFilter = idQuery == null ? "" : idQuery.trim();
        currentPage = 1;
        recomputeRows();
    }

    public void searchByAge(String ageQuery) {
        this.currentAgeFilter = ageQuery == null ? "" : ageQuery.trim();
        currentPage = 1;
        recomputeRows();
    }

    public void searchByAverage(String averageQuery) {
        this.currentAverageFilter = averageQuery == null ? "" : averageQuery.trim();
        currentPage = 1;
        recomputeRows();
    }

    private void recomputeRows() {
        List<StudentRow> computedRows = new ArrayList<>();

        for (StudentRow row : sourceRows) {
            if (!matchesSelectedOptions(row, activeFilters)) {
                continue;
            }

            if (!currentIdFilter.isEmpty()) {
                try {
                    int idToMatch = Integer.parseInt(currentIdFilter);
                    if (row.getId() != idToMatch) {
                        continue;
                    }
                } catch (NumberFormatException exception) {
                    continue;
                }
            }

            if (!currentAgeFilter.isEmpty()) {
                try {
                    int ageToMatch = Integer.parseInt(currentAgeFilter);
                    if (row.getAge() != ageToMatch) {
                        continue;
                    }
                } catch (NumberFormatException exception) {
                    continue;
                }
            }

            if (!currentAverageFilter.isEmpty()) {
                try {
                    double averageToMatch = Double.parseDouble(currentAverageFilter);
                    if (Math.abs(row.getGrade() - averageToMatch) > 0.01) {
                        continue;
                    }
                } catch (NumberFormatException exception) {
                    continue;
                }
            }

            if (!currentQuery.isEmpty() && !matchesQuery(row, currentQuery)) {
                continue;
            }

            computedRows.add(row);
        }

        computedRows.sort(buildComparator(currentSortBy, currentSortOrder));
        allRows = computedRows;
        refreshPage();
    }

    private boolean matchesQuery(StudentRow row, String query) {
        return row.getFirstName().toLowerCase().contains(query)
            || row.getLastName().toLowerCase().contains(query)
            || String.valueOf(row.getAge()).contains(query)
            || String.valueOf(row.getGrade()).contains(query)
            || row.getCreatedAt().toLowerCase().contains(query);
    }

    private Comparator<StudentRow> buildComparator(String sortBy, String sortOrder) {
        Comparator<StudentRow> comparator;

        if (sortBy == null || sortBy.isBlank()) {
            comparator = Comparator.comparingInt(StudentRow::getId);
        } else {
            comparator = switch (sortBy.trim().toLowerCase()) {
                case "nom" -> Comparator.comparing(
                    StudentRow::getLastName,
                    String.CASE_INSENSITIVE_ORDER
                );
                case "prenom" -> Comparator.comparing(
                    StudentRow::getFirstName,
                    String.CASE_INSENSITIVE_ORDER
                );
                case "age" -> Comparator.comparingInt(StudentRow::getAge);
                case "moyenne" -> Comparator.comparingDouble(StudentRow::getGrade);
                default -> Comparator.comparingInt(StudentRow::getId);
            };
        }

        if (sortOrder != null && "decroissant".equalsIgnoreCase(sortOrder.trim())) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private boolean matchesSelectedOptions(StudentRow row, List<String> selectedOptions) {
        if (selectedOptions.isEmpty()) {
            return true;
        }

        for (String option : selectedOptions) {
            if (!matchesOption(row, option)) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesOption(StudentRow row, String option) {
        if (option == null || option.isBlank()) {
            return true;
        }

        String normalized = option.trim().toLowerCase();

        if (normalized.contains("18-25")) {
            return row.getAge() >= 18 && row.getAge() <= 25;
        }

        if (normalized.contains("26-35")) {
            return row.getAge() >= 26 && row.getAge() <= 35;
        }

        if (normalized.contains("36-45")) {
            return row.getAge() >= 36 && row.getAge() <= 45;
        }

        if (normalized.contains("majeur")) {
            return row.getAge() >= 18;
        }

        if (normalized.contains("mineur")) {
            return row.getAge() < 18;
        }

        if (normalized.contains("excellent")) {
            return row.getGrade() >= 16.0;
        }

        if (normalized.contains("admis") || normalized.contains(">=10") || normalized.contains("sup") && normalized.contains("10")) {
            return row.getGrade() >= 10.0;
        }

        if (normalized.contains("echec") || normalized.contains("risque") || normalized.contains("<10") || normalized.contains("inf") && normalized.contains("10")) {
            return row.getGrade() < 10.0;
        }

        return true;
    }

    private void configureTable() {
        root.setAlignment(Pos.TOP_CENTER);
        root.setFillWidth(true);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #E7F0FF, #D9E8FF);"
                + "-fx-border-color: #A8C6EC;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
            + "-fx-padding: 4 4 6 4;"
        );
        applyRoundedClip(root, 12);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.setFixedCellSize(TABLE_ROW_HEIGHT);
        tableView.setMaxWidth(Double.MAX_VALUE);
        tableView.prefWidthProperty().bind(root.widthProperty().subtract(12));
        tableView.setPrefHeight(TABLE_HEADER_HEIGHT + (pageSize * TABLE_ROW_HEIGHT) + TABLE_VERTICAL_BUFFER);
        tableView.addEventFilter(ScrollEvent.ANY, ScrollEvent::consume);
        tableView.setStyle(
            "-fx-background-color: #EAF3FF;"
                + "-fx-border-color: #95B9E8;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
                + "-fx-background-insets: 0;"
            + "-fx-padding: 2;"
        );
        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            hideTableScrollBars();
        });
        hideTableScrollBars();

        TableColumn<StudentRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));

        TableColumn<StudentRow, String> firstNameCol = new TableColumn<>("Prenom");
        firstNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirstName()));

        TableColumn<StudentRow, String> lastNameCol = new TableColumn<>("Nom");
        lastNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLastName()));

        TableColumn<StudentRow, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAge()));

        TableColumn<StudentRow, Double> gradeCol = new TableColumn<>("Moyenne");
        gradeCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getGrade()));
        gradeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-alignment: CENTER;");
                    setOnMouseClicked(null);
                } else {
                    setText(String.format("%.2f", item));
                    setStyle("-fx-alignment: CENTER; -fx-text-fill: #0369A1; -fx-underline: true; -fx-cursor: hand;");
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1 && !isEmpty()) {
                            StudentRow row = getTableRow() == null ? null : getTableRow().getItem();
                            if (row != null) {
                                onGradeClick.accept(row);
                            }
                        }
                    });
                }
            }
        });

        TableColumn<StudentRow, String> createdAtCol = new TableColumn<>("Cree le");
        createdAtCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCreatedAt()));
        createdAtCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(formatTimestampDisplay(item));
            }
        });

        TableColumn<StudentRow, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setSortable(false);
        actionsCol.setReorderable(false);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new CustomButton("Modifier", 95, 28, "#4B5563").build();
            private final Button deleteButton = new CustomButton("X", 34, 28, "#991B1B").build();
            private final HBox buttons = new HBox(8, editButton, deleteButton);

            {
                buttons.setAlignment(Pos.CENTER);
                editButton.setOnAction(event -> {
                    StudentRow row = getCurrentRow();
                    if (row != null) {
                        onEdit.accept(row);
                    }
                });

                deleteButton.setOnAction(event -> {
                    StudentRow row = getCurrentRow();
                    if (row != null && confirmDelete(row)) {
                        onDelete.accept(row);
                    }
                });
            }

            private boolean confirmDelete(StudentRow row) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Supprimer cet eleve ?");
                alert.setContentText(
                    row.getFirstName() + " " + row.getLastName() + " (ID " + row.getId() + ")"
                );
                return alert.showAndWait()
                    .filter(ButtonType.OK::equals)
                    .isPresent();
            }

            private StudentRow getCurrentRow() {
                if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    return null;
                }
                return getTableView().getItems().get(getIndex());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(firstNameCol);
        tableView.getColumns().add(lastNameCol);
        tableView.getColumns().add(ageCol);
        tableView.getColumns().add(gradeCol);
        tableView.getColumns().add(createdAtCol);
        tableView.getColumns().add(actionsCol);

        idCol.setStyle("-fx-alignment: CENTER;");
        ageCol.setStyle("-fx-alignment: CENTER;");
        gradeCol.setStyle("-fx-alignment: CENTER;");
        actionsCol.setStyle("-fx-alignment: CENTER;");

        root.getChildren().clear();
        root.getChildren().add(tableView);
    }

    private void refreshPage() {
        int totalPages = getTotalPages();
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        int fromIndex = Math.min((currentPage - 1) * pageSize, allRows.size());
        int toIndex = Math.min(fromIndex + pageSize, allRows.size());

        List<StudentRow> pageRows = fromIndex < toIndex
            ? new ArrayList<>(allRows.subList(fromIndex, toIndex))
            : List.of();

        tableView.setItems(FXCollections.observableArrayList(pageRows));
        updateTableHeight(pageRows.size());
        tableView.refresh();
        hideTableScrollBars();
        renderPagination(totalPages);
    }

    private void hideTableScrollBars() {
        Platform.runLater(() -> {
            for (Node node : tableView.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar scrollBar) {
                    scrollBar.setVisible(false);
                    scrollBar.setManaged(false);
                    scrollBar.setOpacity(0);
                    scrollBar.setPrefWidth(0);
                    scrollBar.setMaxWidth(0);
                    scrollBar.setMinWidth(0);

                    for (Node child : scrollBar.lookupAll(".increment-button, .decrement-button, .increment-arrow, .decrement-arrow")) {
                        child.setVisible(false);
                        child.setManaged(false);
                        child.setOpacity(0);
                    }
                }
            }
        });
    }

    private void updateTableHeight(int visibleRows) {
        int displayedRows = Math.max(1, visibleRows);
        double computedHeight = TABLE_HEADER_HEIGHT + (displayedRows * TABLE_ROW_HEIGHT) + TABLE_VERTICAL_BUFFER;
        tableView.setPrefHeight(computedHeight);
        tableView.setMinHeight(computedHeight);
        tableView.setMaxHeight(computedHeight);
        tableView.applyCss();
        tableView.layout();
    }

    private void applyRoundedClip(Region region, double radius) {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(radius * 2);
        clip.setArcHeight(radius * 2);
        clip.widthProperty().bind(region.widthProperty());
        clip.heightProperty().bind(region.heightProperty());
        region.setClip(clip);
    }

    private String formatTimestampDisplay(String value) {
        String trimmed = value == null ? "" : value.trim();
        int dotIndex = trimmed.indexOf('.');
        if (dotIndex > 0) {
            return trimmed.substring(0, dotIndex);
        }
        return trimmed;
    }

    private void renderPagination(int totalPages) {
        PaginationBar paginationBar = new PaginationBar(currentPage, totalPages, 460, 44);
        HBox paginationNode = paginationBar.build();

        paginationBar.getPrevButton().setOnAction(event -> {
            if (currentPage > 1) {
                currentPage--;
                refreshPage();
            }
        });

        paginationBar.getNextButton().setOnAction(event -> {
            if (currentPage < totalPages) {
                currentPage++;
                refreshPage();
            }
        });

        if (root.getChildren().size() == 1) {
            root.getChildren().add(paginationNode);
        } else {
            root.getChildren().set(1, paginationNode);
        }
    }

    public static class StudentRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final int age;
        private final double grade;
        private final String createdAt;

        public StudentRow(int id, String firstName, String lastName, int age, double grade, String createdAt) {
            this.id = id;
            this.firstName = firstName == null ? "" : firstName;
            this.lastName = lastName == null ? "" : lastName;
            this.age = age;
            this.grade = grade;
            this.createdAt = createdAt == null ? "" : createdAt;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }

        public double getGrade() {
            return grade;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}

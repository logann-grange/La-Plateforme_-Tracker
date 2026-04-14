package Vue.Components;

import javafx.scene.control.Label;

public class LabelCustom {
	private final String text;
	private final double fontSize;
	private final String textColor;
	private final boolean bold;

	public LabelCustom(String text) {
		this(text, 14, "#1A1A1A", false);
	}

	public LabelCustom(String text, double fontSize, String textColor, boolean bold) {
		this.text = text == null ? "" : text;
		this.fontSize = fontSize;
		this.textColor = textColor == null ? "#1A1A1A" : textColor;
		this.bold = bold;
	}

	public Label build() {
		Label label = new Label(text);
		label.setStyle(
			"-fx-font-size: " + fontSize + ";"
				+ "-fx-text-fill: " + textColor + ";"
				+ "-fx-font-weight: " + (bold ? "bold" : "normal") + ";"
		);
		return label;
	}
}

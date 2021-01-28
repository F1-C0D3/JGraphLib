package de.jgraphlib.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TerminalPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane();

	ArrayList<String> history = new ArrayList<String>();
	private int historyIndex = 0;
	private int inputStartPosition = 2;
	Consumer<String> inputListener;
	String prompt = "_ ";

	public TerminalPanel(Font font, Color color) {
		this.setLayout(new BorderLayout());

		this.scrollPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		this.textArea.setFont(font);
		this.textArea.setOpaque(false);
		this.textArea.setBackground(new Color(0, 0, 0, 0));
		this.textArea.setForeground(color);
		this.textArea.setCaretColor(color);

		this.scrollPane.setOpaque(false);
		this.scrollPane.setBackground(new Color(0, 0, 0, 0));
		this.scrollPane.getViewport().setOpaque(false);
		this.scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
		this.scrollPane.setViewportView(textArea);

		this.add(scrollPane);

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("control A"), "none");

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("UP"), TerminalPanelActions.HISTORY_UP);
		this.textArea.getActionMap().put(TerminalPanelActions.HISTORY_UP,
				new TerminalPanelAction(TerminalPanelActions.HISTORY_UP));

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), TerminalPanelActions.HISTORY_DOWN);
		this.textArea.getActionMap().put(TerminalPanelActions.HISTORY_DOWN,
				new TerminalPanelAction(TerminalPanelActions.HISTORY_DOWN));

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), TerminalPanelActions.CARET_TO_LEFT);
		this.textArea.getActionMap().put(TerminalPanelActions.CARET_TO_LEFT,
				new TerminalPanelAction(TerminalPanelActions.CARET_TO_LEFT));

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), TerminalPanelActions.CARET_TO_RIGHT);
		this.textArea.getActionMap().put(TerminalPanelActions.CARET_TO_RIGHT,
				new TerminalPanelAction(TerminalPanelActions.CARET_TO_RIGHT));

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), TerminalPanelActions.EXECUTE_COMMAND);
		this.textArea.getActionMap().put(TerminalPanelActions.EXECUTE_COMMAND,
				new TerminalPanelAction(TerminalPanelActions.EXECUTE_COMMAND));

		this.textArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), TerminalPanelActions.DELETE_CHARACTER);
		this.textArea.getActionMap().put(TerminalPanelActions.DELETE_CHARACTER,
				new TerminalPanelAction(TerminalPanelActions.DELETE_CHARACTER));

		this.showPrompt();
	}

	private enum TerminalPanelActions {
		HISTORY_UP, HISTORY_DOWN, CARET_TO_LEFT, CARET_TO_RIGHT, EXECUTE_COMMAND, DELETE_CHARACTER;
	}

	public JTextArea getTextArea() {
		return this.textArea;
	}

	private class TerminalPanelAction extends AbstractAction {

		TerminalPanelActions action;

		public TerminalPanelAction(TerminalPanelActions action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (action) {
			case HISTORY_UP:
				textArea.setText(textArea.getText().substring(0, inputStartPosition) + history.get(historyIndex));
				if (historyIndex > 0)
					historyIndex--;
				break;
			case HISTORY_DOWN:
				textArea.setText(textArea.getText().substring(0, inputStartPosition) + history.get(historyIndex));
				if (historyIndex < history.size() - 1)
					historyIndex++;
				break;
			case CARET_TO_RIGHT:
				if (textArea.getCaretPosition() < textArea.getText().length())
					textArea.setCaretPosition(textArea.getCaretPosition() + 1);
				break;
			case CARET_TO_LEFT:
				if (textArea.getCaretPosition() > inputStartPosition)
					textArea.setCaretPosition(textArea.getCaretPosition() - 1);
				break;
			case DELETE_CHARACTER:
				if (textArea.getCaretPosition() > inputStartPosition) {
					StringBuilder stringBuilder = new StringBuilder(textArea.getText());
					int caretPosition = textArea.getCaretPosition();
					stringBuilder.deleteCharAt(caretPosition - 1);
					textArea.setText(stringBuilder.toString());
					textArea.setCaretPosition(caretPosition - 1);
				}
				break;
			case EXECUTE_COMMAND:
				disableTerminal();
				executeCommand(extractCommand());
				showNewLine();
				showPrompt();
				enableTerminal();
				break;
			default:
				break;
			}
		}
	}

	public void setText(String text) {
		this.textArea.setText(text);
		this.showPrompt();
	}

	public void appendText(String text) {
		this.textArea.append(text);
	}

	public void addInputListener(Consumer<String> inputListener) {
		this.inputListener = inputListener;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		Rectangle r = g.getClipBounds();
		g.fillRect(r.x, r.y, r.width, r.height);
		super.paintComponent(g);
	}

	public void clear() {
		textArea.setText("");
	}

	private void showPrompt() {
		textArea.setText(textArea.getText() + prompt);
		textArea.setCaretPosition(textArea.getText().length());
		inputStartPosition = textArea.getCaretPosition();
	}

	private void showNewLine() {
		textArea.setText(textArea.getText() + System.lineSeparator());
	}

	public void enableTerminal() {
		textArea.setEnabled(true);
	}

	public void disableTerminal() {
		textArea.setEnabled(false);
	}

	private void executeCommand(String command) {
		history.add(command);
		historyIndex = history.size() - 1;
		if (inputListener != null)
			inputListener.accept(command);
	}

	private String extractCommand() {
		removeLastLineSeparator();
		String newCommand = stripPreviousCommands();
		return newCommand;
	}

	private void removeLastLineSeparator() {
		String terminalText = textArea.getText();
		if (terminalText.endsWith(System.lineSeparator())) {
			terminalText = terminalText.substring(0, terminalText.length() - 1);
			textArea.setText(terminalText);
		}
	}

	private String stripPreviousCommands() {
		String terminalText = textArea.getText();
		int lastPromptIndex = terminalText.lastIndexOf('_') + 2;
		if (lastPromptIndex < 0 || lastPromptIndex >= terminalText.length())
			return "";
		else
			return terminalText.substring(lastPromptIndex);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Terminal");
				TerminalPanel term = new TerminalPanel(new Font("Consolas", Font.PLAIN, 20), Color.WHITE);
				frame.setBounds(0, 0, 1000, 500);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.getContentPane().add(term);
				term.showPrompt();
			}
		});
	}
}
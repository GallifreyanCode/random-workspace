package be.gallifreyan.eclipse.backup.plugin.handler;

import java.awt.Toolkit;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class PromptPanel extends ApplicationWindow {
	private Composite parent;
	private Composite buttonPanel;
	private Backup backup;

	public PromptPanel(Shell parentShell) {
		super(parentShell);
	}
	
	public void show(Backup backup) {
		this.backup = backup;
		open();
	}

	protected Control createContents(Composite parent) {
		this.parent = parent;
		loadSettings();
		addComponents();
		return parent;
	}
	
	private void addComponents() {
		buttonPanel = new Composite(parent, SWT.CENTER);
		buttonPanel.setLayout(getRowLayout());

		for (Destination destination : Destination.values()) {
			createButton(destination).addListener(
					SWT.Selection,
					createListener(destination)
			);
		}
	}

	private void loadSettings() {
		getShell().setText("Backup Plugin");
		int panelWidth = 400;
		int panelHeight = 130;
		parent.setSize(panelWidth, panelHeight);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
		parent.setLocation(width - (panelWidth / 2), height - (panelHeight / 2));
	}

	private Button createButton(Destination destination) {
		Button btn = new Button(buttonPanel, SWT.NULL);
		Image image = getImage(destination.toString().toLowerCase() + ".png");
		btn.setImage(image);
		return btn;
	}
	
	private Listener createListener(final Destination destination) {
		return new Listener() {
			public void handleEvent(Event event) {
				backup.createPackage(destination);
			}
		};
	}

	private Image getImage(String name) {
		return new Image(parent.getDisplay(), this.getClass()
				.getResourceAsStream(name));
	}

	private static RowLayout getRowLayout() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.wrap = true;
		rowLayout.pack = true;
		rowLayout.justify = true;
		return rowLayout;
	}
}

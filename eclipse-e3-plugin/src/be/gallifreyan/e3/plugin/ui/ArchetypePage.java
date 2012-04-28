package be.gallifreyan.e3.plugin.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import be.gallifreyan.e3.plugin.mvn.creation.CreateProject;
import be.gallifreyan.e3.plugin.mvn.model.MavenArchetype;
import be.gallifreyan.e3.plugin.mvn.model.Project;

public class ArchetypePage extends WizardPage {

	protected ArchetypePage(String pageName) {
		super(pageName);
		setTitle("Custom Archetypes");
		setDescription("Please select a Maven archetype");
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		rowLayout.wrap = true;
		rowLayout.pack = true;
		rowLayout.justify = true;

		composite.setLayout(rowLayout);
		setControl(composite);

		Table table = createTable(composite);
		Label linkedInfoText = createlinkedInfoText(composite);
		addTableListener(table, linkedInfoText);

		// buildProject();
	}

	private Table createTable(Composite composite) {
		Table table = new Table(composite, SWT.PUSH);
		table.setHeaderVisible(true);
		String[] titles = { "Col 1", "Col 2", "Col 3", "Col 4" };

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[loopIndex]);
		}

		readArchetypeData(table);

		// for (int loopIndex = 0; loopIndex < 8; loopIndex++) {
		// createTableItem(table, loopIndex);
		// }
		//
		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}
		
		table.setLayoutData(new RowData(500, 200));
		return table;
	}

	private void readArchetypeData(Table table) {
		for (int loopIndex = 0; loopIndex < 8; loopIndex++) {
			createTableItem(table, loopIndex);
		}
	}

	private TableItem createTableItem(Table table, int loopIndex) {
		TableItem item = new TableItem(table, SWT.NULL);
		item.setText("Item " + loopIndex);
		item.setText(0, "Item " + loopIndex);
		item.setText(1, "Yes");
		item.setText(2, "No");
		item.setText(3, "A table item");
		return item;
	}

	private Table addTableListener(Table table, final Label linkedInfoText) {
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					linkedInfoText.setText("You checked " + event.item);
				} else {
					linkedInfoText.setText("You selected " + event.item);
				}
			}
		});
		return table;
	}

	private Label createlinkedInfoText(Composite composite) {
		Label text = new Label(composite, SWT.PUSH);
		text.setLayoutData(new RowData(220, 30));
		return text;
	}

}

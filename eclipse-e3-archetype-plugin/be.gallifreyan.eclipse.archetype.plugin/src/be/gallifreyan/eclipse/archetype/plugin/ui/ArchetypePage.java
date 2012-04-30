package be.gallifreyan.eclipse.archetype.plugin.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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

import be.gallifreyan.eclipse.archetype.plugin.mvn.creation.CustomArchetypes;
import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Archetype;

public class ArchetypePage extends WizardPage {
	private List<Archetype> archetypeList = new ArrayList<Archetype>();
	
	protected ArchetypePage(String pageName) {
		super(pageName);
		setTitle("Custom Archetypes");
		setDescription("Please select a Maven archetype");
		setPageComplete(false);
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
		//linkedInfoText.setSize(400, 20);
		addTableListener(table, linkedInfoText);

		// buildProject();
	}

	private Table createTable(Composite composite) {
		Table table = new Table(composite, SWT.PUSH);
		table.setHeaderVisible(true);
		String[] titles = {"artifactId", "groupId", "version"};

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[loopIndex]);
		}

		readArchetypeData(table);

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}
		
		table.setLayoutData(new RowData(500, 200));
		return table;
	}

	/**
	 * Will fetch all data from the catalog file and then pass it to the table component.
	 * @param table
	 */
	private void readArchetypeData(Table table) {
		installArchetype();

		archetypeList = CustomArchetypes.list();
		addDataToTable(table);
	}
	
	/**
	 * Method will trigger the installation process of archetypes.
	 */
	private void installArchetype() {
		try {
			ProgressMonitorDialog p = new ProgressMonitorDialog(this.getShell());
			p.run(true, true, new CustomArchetypes());
		
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void addDataToTable(Table table) {
		/*
		 * add them to table
		 */
		for (int loopIndex = 0; loopIndex < archetypeList.size(); loopIndex++) {
			createTableItem(table, loopIndex);
		}
	}

	private TableItem createTableItem(Table table, int loopIndex) {
		Archetype currentArchetype = archetypeList.get(loopIndex);
		TableItem item = new TableItem(table, SWT.NULL);
		
		item.setText(currentArchetype.toString());
		item.setText(0, currentArchetype.getArchetypeArtifactId());
		item.setText(1, currentArchetype.getArchetypeGroupId());
		item.setText(2, currentArchetype.getArchetypeVersion());
		return item;
	}

	private Table addTableListener(Table table, final Label linkedInfoText) {
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				setPageComplete(true);
				if (event.detail == SWT.CHECK) {
					linkedInfoText.setText(getDescriptionFromArchetype(event.item.toString()));
				} else {
					linkedInfoText.setText(getDescriptionFromArchetype(event.item.toString()));
				}
			}
		});
		return table;
	}
	
	private String getDescriptionFromArchetype(String artifactId) {
		String description = "No description.";
		for(Archetype archetype : archetypeList){
			if(archetype.getArchetypeArtifactId().equals(parseWidgetName(artifactId))){
				description = archetype.getDescription();
			}
		}
		return description;
	}
	
	private String parseWidgetName(String widgetName) {
		return widgetName.replace("TableItem {", "").replace("}", "");
	}

	private Label createlinkedInfoText(Composite composite) {
		Label text = new Label(composite, SWT.PUSH);
		text.setLayoutData(new RowData(220, 30));
		return text;
	}

	
}

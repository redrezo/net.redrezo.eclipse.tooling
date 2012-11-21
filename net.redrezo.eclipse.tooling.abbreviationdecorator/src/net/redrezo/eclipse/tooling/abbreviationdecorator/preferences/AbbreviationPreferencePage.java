package net.redrezo.eclipse.tooling.abbreviationdecorator.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class AbbreviationPreferencePage extends PreferencePage
implements IWorkbenchPreferencePage {

	private final static String ROOT_NODE_KEY = "net.redrezo.eclipse.tooling.abbreviationdecorator.preferences";
	
	private IEclipsePreferences rootNode;
	
	private TableViewer mappingsViewer;
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
		
		rootNode = ConfigurationScope.INSTANCE.getNode(ROOT_NODE_KEY);
		rootNode.node("mappings").node("example").put("regex", "gen");
		rootNode.node("mappings").node("example").put("replacement", "neg");
//				// We access the Configuration Scope
//				Preferences preferences = ConfigurationScope.INSTANCE
//				  .getNode("de.vogella.preferences.test");
//
//				Preferences sub1 = preferences.node("node1");
//				Preferences sub2 = preferences.node("node2");
//				sub1.put("h1", "Hello");
//				sub1.put("h2", "Hello again");
//				sub2.put("h1", "Moin");
//				try {
//				  // Forces the application to save the preferences
//				  preferences.flush();
//				  } catch (BackingStoreException e) {
//				    e.printStackTrace();
//				  }
//				} 
//		// ACCESS
//		String text = Platform.getPreferencesService().
//				  getString("de.vogella.preferences.page", "MySTRING1", "hello", null); 
	}

	  private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn(mappingsViewer,
		        SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(true);
		    return viewerColumn;
		  }

	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite content = new Composite(parent, SWT.NONE);
		
		content.setLayout(new GridLayout(2, false));

		Label nfo = new Label(content, SWT.NONE);
		nfo.setText("Editor is not finished yet -.-");
		GridData l = new GridData(SWT.FILL, SWT.FILL, false, true);
		l.horizontalSpan = 2;
		nfo.setLayoutData(l);
		
		mappingsViewer = new TableViewer(content);
		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableData.verticalSpan = 3;
		mappingsViewer.getTable().setLayoutData(tableData);
		mappingsViewer.getTable().setHeaderVisible(true);
		
		 // First column is for the first name
	    TableViewerColumn col = createTableViewerColumn("Name", 100, 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	    	  IEclipsePreferences e = (IEclipsePreferences) element;
	    	  return e.name();
	      }
	    });
	    col.setEditingSupport(new EditingSupport(mappingsViewer){
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(mappingsViewer.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return false;
			}

			@Override
			protected Object getValue(Object element) {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	 return e.name();
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	String originalName = e.name();
		    	
		    	Preferences newNode = e.parent().node((String) value);
		    	for (String key : e.keys()) {
		    		newNode.put(key, e.get(key, null));
		    	}
		    	
		    	e.removeNode();
		    	
		    	
				}
				catch (BackingStoreException e) {
					e.printStackTrace();
				}
		    	
				mappingsViewer.refresh();
			}
	    	
	    });

	    // Second column is for the last name
	    col = createTableViewerColumn("Regex", 100, 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	    	  IEclipsePreferences e = (IEclipsePreferences) element;
	    	  return e.get("regex", "<not defined>");
	      }
	    });
	    col.setEditingSupport(new EditingSupport(mappingsViewer){
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(mappingsViewer.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	 String value =  (String) e.get("regex", "<not defined>");
		    	System.err.println("value = " + value);
		    	return value;
			}

			@Override
			protected void setValue(Object element, Object value) {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	 e.put("regex", (String) value);
		    	 mappingsViewer.update(element, null);
			}
	    	
	    });
	    
	    // Now the gender
	    col = createTableViewerColumn("Replacement", 100, 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	    	  IEclipsePreferences e = (IEclipsePreferences) element;
	    	  return e.get("replacement", "<not defined>");
	      }
	    });
	    col.setEditingSupport(new EditingSupport(mappingsViewer){
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(mappingsViewer.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	String value =  e.get("replacement", "<not defined>");
		    	System.err.println(value);
		    	return value;
			}

			@Override
			protected void setValue(Object element, Object value) {
				IEclipsePreferences e = (IEclipsePreferences) element;
		    	 e.put("replacement", (String) value);
		    	
		    	 mappingsViewer.update(element, null);
			}
	    	
	    });
		
		mappingsViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				IEclipsePreferences in  = (IEclipsePreferences) inputElement;
				System.err.println("input is " + in);
				try {
					String[] keys = in.keys();
					System.err.println("children " + Arrays.toString(keys));
					List<Object> result = new ArrayList<Object>();
					
					for (String key: keys) {
						result.add(in.node(key));
					}
					
					return result.toArray();
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
				
				return new Object[0];
			}
		});
		mappingsViewer.setInput(rootNode.node("mappings"));
		
		GridData buttonData = new GridData(SWT.FILL, SWT.FILL, false, false);
		
		Button add = new Button(content, SWT.NONE);
		add.setText("Add");
		add.setLayoutData(GridDataFactory.copyData(buttonData));
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createNewEntry();
			}
		});
		Button delete = new Button(content, SWT.NONE);
		delete.setText("Delete");
		delete.setLayoutData(GridDataFactory.copyData(buttonData));
		
		mappingsViewer.getTable().setEnabled(true);
		add.setEnabled(false);
		delete.setEnabled(false);
		
		return content;
	}

	private void createNewEntry() {
		
		
		
		rootNode.node("mappings").put("somekey", "somevalue");
		mappingsViewer.refresh();
	}

}

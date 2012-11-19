package net.redrezo.eclipse.tooling.abbreviationdecorator.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class AbbreviationPreferencePage extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	public AbbreviationPreferencePage() {
		super(GRID);
		//setPreferenceStore();
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createFieldEditors() {
		AbbreviationListEditor editor = new AbbreviationListEditor("Hallo", "Welt", getFieldEditorParent());
		addField(editor);
	}

}

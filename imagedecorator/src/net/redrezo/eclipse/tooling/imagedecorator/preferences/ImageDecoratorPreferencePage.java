package net.redrezo.eclipse.tooling.imagedecorator.preferences;

import net.redrezo.eclipse.tooling.imagedecorator.Activator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ImageDecoratorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public ImageDecoratorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void createFieldEditors() {
		ImageListEditor editor = new ImageListEditor(
				PreferenceConstants.FILE_EXTENSIONS, "File Extensions",
				getFieldEditorParent());
		addField(editor);
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}
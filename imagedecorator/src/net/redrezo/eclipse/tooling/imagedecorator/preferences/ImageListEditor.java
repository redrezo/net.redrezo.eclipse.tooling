package net.redrezo.eclipse.tooling.imagedecorator.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

public class ImageListEditor extends ListEditor {

	public ImageListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer sb = new StringBuffer();
		if (items != null && items.length > 0) {
			sb.append(items[0]);
		}
		for (int i = 1; i < items.length; i++) {
			sb.append(PreferenceConstants.FILE_EXTENSIONS_SEPARATOR + items[i]);
		}
		return sb.toString();
	}

	@Override
	protected String getNewInputObject() {
		InputDialog dialog = new InputDialog(getShell(), "Add File Extension",
				"bla", "", new IInputValidator() {

					@Override
					public String isValid(String newText) {
						if (newText != null && newText.trim().length() > 2
								&& newText.trim().length() < 20) {
							return null;
						} else {
							return "not valid";
						}
					}
				});

		if (dialog.open() == IDialogConstants.OK_ID) {
			return dialog.getValue();
		} else {
			return null;
		}
	}

	@Override
	protected String[] parseString(String stringList) {
		return stringList.split(PreferenceConstants.FILE_EXTENSIONS_SEPARATOR);
	}
}

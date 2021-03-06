package com.mda.bomb.screen.filter;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

public class DigitFilter implements TextFieldFilter {

	private char[] accepted;

	public DigitFilter() {
		accepted = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };
	}

	@Override
	public boolean acceptChar(TextField textField, char c) {
		for (char a : accepted)
			if (a == c)
				return true;
		return false;
	}
}
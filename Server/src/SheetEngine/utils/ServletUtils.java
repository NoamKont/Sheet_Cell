package SheetEngine.utils;


import body.Logic;
import body.Sheets.SheetsManager;
import body.impl.ImplLogic;
import body.users.UserManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static SheetEngine.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String SHEET_MANAGER_ATTRIBUTE_NAME = "sheetManager";
	private static final String ENGINE_ATTRIBUTE_NAME = "engine";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object sheetManagerLock = new Object();
	private static final Object engineLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
	public static SheetsManager getSheetManager(ServletContext servletContext) {

		synchronized (sheetManagerLock) {
			if (servletContext.getAttribute(SHEET_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SHEET_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (SheetsManager) servletContext.getAttribute(SHEET_MANAGER_ATTRIBUTE_NAME);
	}

	public static Logic getEngine(ServletContext servletContext) {
		synchronized (engineLock) {
			if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new ImplLogic());
			}
		}
		return (Logic) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}

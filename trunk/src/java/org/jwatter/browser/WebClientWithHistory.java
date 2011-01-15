/*
Copyright 2011 Karl-Michael Schneider

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jwatter.browser;

import java.util.ArrayList;
import java.util.HashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;

/**
 * This class adds a history to an HtmlUnit web client so you can simulate
 * clicking back and forward buttons.
 * 
 * @author kschneider
 * 
 */
public class WebClientWithHistory extends WebClient {

	private static final long serialVersionUID = 1L;

	protected BrowserHistory browserHistory;
	protected WebWindowListener webWindowForBrowserHistoryListener;

	/**
	 * Creates a web client using the default browser version.
	 */
	public WebClientWithHistory () {
		this(BrowserVersion.getDefault());
	}

	/**
	 * Creates a web client using the specified browser version.
	 * 
	 * @param version
	 *            the browser version to simulate
	 */
	public WebClientWithHistory (BrowserVersion version) {
		super(version);
		browserHistory = new BrowserHistory();
		webWindowForBrowserHistoryListener =
				new WebWindowForBrowserHistoryListener(browserHistory);
		addWebWindowListener(webWindowForBrowserHistoryListener);
		WebWindow window = getCurrentWindow();
		webWindowForBrowserHistoryListener.webWindowOpened(
				new WebWindowEvent(window, WebWindowEvent.OPEN,
						null, window.getEnclosedPage()));
	}

	/**
	 * Navigate to the previous (older) page in the browser's history for
	 * the current window.
	 * 
	 * @return the previous page
	 * @throws BrowserHistoryException
	 *             if no browser window is open, or the history for the
	 *             current window is empty, or the current window already
	 *             contains the oldest page in the window's history
	 */
	@SuppressWarnings("unchecked")
	public <P extends Page> P back () throws BrowserHistoryException {
		P previous = (P) browserHistory.back();
		super.getCurrentWindow().setEnclosedPage(previous);
		return previous;
	}

	/**
	 * Navigate to the next (more recent) page in the browser's history for
	 * the current window.
	 * 
	 * @return the next page
	 * @throws BrowserHistoryException
	 *             if no browser window is open, or the history for the
	 *             current window is empty, or the current window already
	 *             contains the most recent page from the window's history.
	 */
	@SuppressWarnings("unchecked")
	public <P extends Page> P forward () throws BrowserHistoryException {
		P next = (P) browserHistory.forward();
		super.getCurrentWindow().setEnclosedPage(next);
		return next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gargoylesoftware.htmlunit.WebClient#setCurrentWindow(com.
	 * gargoylesoftware.htmlunit.WebWindow)
	 */
	@Override
	public void setCurrentWindow (WebWindow window) {
		super.setCurrentWindow(window);
		try {
			browserHistory.setCurrentWindow(window);
		} catch( BrowserHistoryException e ) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A listener that receives WebWindowEvents from the web client and updates the
	 * browser history.
	 * 
	 * @author kschneider
	 * 
	 */
	protected class WebWindowForBrowserHistoryListener implements WebWindowListener {

		protected BrowserHistory browserHistory;
		
		public WebWindowForBrowserHistoryListener (BrowserHistory history) {
			browserHistory = history;
		}

		public void webWindowOpened (WebWindowEvent event) {
			try {
				browserHistory.addWindow(event.getWebWindow(), true);
			} catch( BrowserHistoryException e ) {
				throw new RuntimeException(e);
			}
		}
		
		public void webWindowClosed (WebWindowEvent event) {
			try {
				browserHistory.removeWindow(event.getWebWindow());
			} catch( BrowserHistoryException e ) {
				throw new RuntimeException(e);
			}
		}

		public void webWindowContentChanged (WebWindowEvent event) {
			try {
				browserHistory.addPage(event.getNewPage(), event.getWebWindow());
			} catch( BrowserHistoryException e ) {
				throw new RuntimeException(e);
			}
		}

	}

	protected class BrowserHistory {

		protected HashMap<WebWindow, ArrayList<Page>> pageHistory;
		protected HashMap<WebWindow, Integer> currentPage;
		protected WebWindow currentWindow;

		public BrowserHistory () {
			pageHistory = new HashMap<WebWindow, ArrayList<Page>>();
			currentPage = new HashMap<WebWindow, Integer>();
			currentWindow = null;
		}

		public void addWindow (WebWindow window) throws BrowserHistoryException {
			addWindow(window, false);
		}

		public void addWindow (WebWindow window, boolean setCurrentWindow)
				throws BrowserHistoryException {
			if( containsWindow(window) ) {
				throw new BrowserHistoryException("Window already in browser history");
			}
			pageHistory.put(window, new ArrayList<Page>());
			currentPage.put(window, -1);
			if( setCurrentWindow ) {
				setCurrentWindow(window);
			}
			Page page = window.getEnclosedPage();
			if( page != null ) {
				addPage(page, window);
			}
		}
		
		public void removeWindow (WebWindow window) throws BrowserHistoryException {
			if( !containsWindow(window) ) {
				throw new BrowserHistoryException("Window not in browser history");
			}
			pageHistory.remove(window);
			currentPage.remove(window);
			pageHistory.get(window);
			if( currentWindow == window ) {
				currentWindow = null;
			}
		}

		public void removeAllWindows () {
			pageHistory.clear();
			currentPage.clear();
			currentWindow = null;
		}
		
		public boolean containsWindow (WebWindow window) {
			return pageHistory.containsKey(window);
		}
		
		public int size() {
			return pageHistory.size();
		}

		public void setCurrentWindow (WebWindow window)
				throws BrowserHistoryException {
			if( !pageHistory.containsKey(window) ) {
				addWindow(window);
			}
			currentWindow = window;
		}

		public void addPage (Page page) throws BrowserHistoryException {
			if( currentWindow == null ) {
				throw new BrowserHistoryException(
						"No window in browser history");
			}
			addPage(page, currentWindow);
		}

		public void addPage (Page page, WebWindow window)
				throws BrowserHistoryException {
			if( !containsWindow(window) ) {
				addWindow(window, false);
			}
			ArrayList<Page> history = pageHistory.get(window);
			int index = currentPage.get(window);
			if( index > -1 ) {
				history.subList(index + 1, history.size()).clear();
			}
			history.add(page);
			index++;
			currentPage.put(window, index);
			assert (currentPage.get(window) == pageHistory.get(window).size() - 1);
		}

		public Page back () throws BrowserHistoryException {
			if( currentWindow == null ) {
				throw new BrowserHistoryException(
						"No window in browser history");
			}
			int index = currentPage.get(currentWindow);
			if( index == -1 ) {
				throw new BrowserHistoryException("Empty history");
			}
			if( index == 0 ) {
				throw new BrowserHistoryException(
						"Cannot go back, already at oldest page in history");
			}
			index--;
			currentPage.put(currentWindow, index);
			return pageHistory.get(currentWindow).get(index);
		}

		public Page forward () throws BrowserHistoryException {
			if( currentWindow == null ) {
				throw new BrowserHistoryException(
						"No window in browser history");
			}
			int index = currentPage.get(currentWindow);
			if( index == -1 ) {
				throw new BrowserHistoryException("Empty history");
			}
			if( index == pageHistory.get(currentWindow).size() - 1 ) {
				throw new BrowserHistoryException(
						"Cannot go forward, already at most recent page in history");
			}
			index++;
			currentPage.put(currentWindow, index);
			return pageHistory.get(currentWindow).get(index);
		}
	}
	
}


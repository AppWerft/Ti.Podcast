/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.podcast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

@Kroll.module(name = "Tipodcast", id = "de.appwerft.podcast")
public class TipodcastModule extends KrollModule {
	private final class SoupRequestHandler extends
			AsyncTask<Void, Void, KrollDict> {
		private final KrollFunction mCallback;
		private final KrollDict options;

		private SoupRequestHandler(KrollFunction mCallback, KrollDict options) {
			this.mCallback = mCallback;
			this.options = options;
		}

		@Override
		protected KrollDict doInBackground(Void[] arg) {
			int timeout = 10000;
			String url = null;
			String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:46.0) Gecko/20100101 Firefox/46.0";
			if (options.containsKeyAndNotNull("timeout")) {
				timeout = options.getInt("timeout");
			}
			if (options.containsKeyAndNotNull(TiC.PROPERTY_URL)) {
				url = options.getString(TiC.PROPERTY_URL);
			}
			if (options.containsKeyAndNotNull("userAgent")) {
				userAgent = options.getString("userAgent");
			}
			KrollDict resultDict = new KrollDict();
			Document doc = null;
			try {
				doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout)
						.ignoreContentType(false).get();
			} catch (IOException e) {
				e.printStackTrace();
				resultDict.put("error", e.getMessage());
				return resultDict;
			}
			if (doc == null) {
				resultDict.put("error", "null");
				return resultDict;
			}
			final String[] keys = { "title", "description", "link", "category",
					"copyright", "pubDate", "lastBuildDate", "itunes|subtitle" };
			for (String key : keys) {
				Element elem = doc.select("channel > " + key).first();
				if (elem != null)
					resultDict.put(key.replace("itunes|", ""), elem.text());
			}
			/* getting items */
			List<KrollDict> itemArray = new ArrayList<KrollDict>();
			Elements elements = doc.select("channel > item");
			for (Element element : elements) {
				KrollDict o = new KrollDict();
				final String[] subkeys = { "title", "description", "link",
						"itunes|author", "itunes|duration", "pubDate", "guid" };
				for (String subkey : subkeys) {
					Element subelem = element.select(subkey).first();
					if (subelem != null)
						o.put(subkey.replace("itunes|", ""), subelem.text());

				}
				KrollDict enclosure = new KrollDict();
				enclosure.put("url", element.select("enclosure").attr("url"));
				enclosure.put("length",
						element.select("enclosure").attr("length"));
				enclosure.put("type", element.select("enclosure").attr("type"));
				o.put("enclosure", enclosure);
				itemArray.add(o);
			}
			resultDict.put("items", itemArray.toArray());
			return resultDict;
		}

		protected void onPostExecute(KrollDict resultDict) {
			if (mCallback != null)
				mCallback.call(getKrollObject(), resultDict);
		}
	}

	private static final String LCAT = "Podcastsauger";
	public KrollFunction mCallback;

	public TipodcastModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
	}

	@Kroll.method
	public void loadPodcast(final KrollDict options,
			final @Kroll.argument(optional = true) KrollFunction mCallback) {
		Log.d(LCAT, options.toString());
		AsyncTask<Void, Void, KrollDict> doRequest = new SoupRequestHandler(
				mCallback, options);
		doRequest.execute();
	}
}

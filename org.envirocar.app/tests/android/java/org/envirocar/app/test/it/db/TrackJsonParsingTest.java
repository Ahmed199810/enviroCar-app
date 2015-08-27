package org.envirocar.app.test.it.db;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import junit.framework.Assert;

import org.envirocar.app.json.TrackDecoder;
import org.envirocar.app.storage.DbAdapterImpl;
import org.envirocar.app.storage.Track;
import org.envirocar.app.storage.Track.TrackStatus;
import org.envirocar.app.test.ResourceLoadingTestCase;
import org.json.JSONException;

public class TrackJsonParsingTest extends ResourceLoadingTestCase {

	public void testParsingAndResolving() throws NumberFormatException, JSONException, ParseException, IOException, InstantiationException {
		DbAdapterImpl dba = new DbAdapterImpl(getInstrumentation().getContext());

		Track t = new TrackDecoder().fromJson(createJsonViaStream());
		dba.insertTrack(t, true);
		
		Track dbTrack = dba.getTrack(t.getTrackId());
		
		Assert.assertTrue("Car was null!", dbTrack.getCar() != null);
		Assert.assertTrue("Track contained no measurements!", dbTrack.getMeasurements() != null &&
				dbTrack.getMeasurements().size() > 0);
		Assert.assertTrue("Track contained wrong number of measurements!", dbTrack.getMeasurements().size() == 3);
		Assert.assertTrue("Track not set as FINISHED!", dbTrack.getStatus() == TrackStatus.FINISHED);

		dba.deleteTrack(dbTrack.getTrackId());

		Assert.assertTrue("Expected an empty list!", dba.getAllMeasurementsForTrack(dbTrack).isEmpty());
	}

	private InputStream createJsonViaStream() throws IOException {
		return getInstrumentation().getContext().getAssets().open("/track_mockup.json");
	}
	
}

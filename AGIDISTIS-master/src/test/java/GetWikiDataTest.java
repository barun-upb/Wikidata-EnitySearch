import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.aksw.agdistis.util.GetWikiData;
import org.aksw.agdistis.util.WikiData;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetWikiDataTest {

	Logger log = LoggerFactory.getLogger(TripleIndexTest.class);
	private GetWikiData index;

	@Before
	public void init() {
		try {
			index = new GetWikiData();

		} catch (Exception e) {
			log.error(
					"Can not load index or DBpedia repository due to either wrong properties in agdistis.properties or missing index at location", e);
		}
	}

	@Test
	public void SendData() {
		try {
			List<WikiData> type = index.sendData("PadErborN");
			assertTrue(type.size() > 0);
			for (WikiData t : type) {
				log.debug(t.toString());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	Failed test
	@Test
	public void emptyData() {
		try {
			List<WikiData> type = index.sendData(null);
			assertTrue(type.size() > 0);
			for (WikiData t : type) {
				log.debug(t.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void longSentenceData() {
		try {
			List<WikiData> type = index.sendData("Dresden Railway station");
			assertTrue(type.size() > 0);
			for (WikiData t : type) {
				log.debug(t.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	Failed Test
	@Test
	public void numberData() {
		try {
			List<WikiData> type = index.sendData("213122312");
			assertTrue(type.size() > 0);
			for (WikiData t : type) {
				log.debug(t.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	Failed
	@Test
	public void irrelevantData() {
		try {
			List<WikiData> type = index.sendData("sdasdasdas");
			assertTrue(type.size() > 0);
			for (WikiData t : type) {
				log.debug(t.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

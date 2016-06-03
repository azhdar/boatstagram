package com.test.boatstagram.data;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoatStagramItemTest {

    BoatStagramItem boatStagramItem;

    @Before
    public void setUp() throws Exception {
        JSONObject jsonObject = new JSONObject("{\"code\": \"BFR8iqhNNF2\", \"dimensions\": {\"width\": 1080, \"height\": 1080}, \"owner\": {\"id\": \"361518379\"}, \"comments\": {\"count\": 0}, \"caption\": \"Horizon... #veracruz #playa #sea #mar #boat #traveltheworld #travelling #mexico #mexico_maravilloso #blue #pinetree #travelgram #ontheroad #landscape #horizon #faraway #travelgram\", \"likes\": {\"count\": 1}, \"date\": 1462998560.0, \"thumbnail_src\": \"https://scontent-fra3-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/13167244_109297296152737_2019378246_n.jpg?ig_cache_key=MTI0ODA0NDgzNjY2NTQ3OTU0Mg%3D%3D.2\", \"is_video\": false, \"id\": \"1248044836665479542\", \"display_src\": \"https://scontent-fra3-1.cdninstagram.com/t51.2885-15/e35/13167244_109297296152737_2019378246_n.jpg?ig_cache_key=MTI0ODA0NDgzNjY2NTQ3OTU0Mg%3D%3D.2\"}");

        boatStagramItem = new BoatStagramItem(jsonObject);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(boatStagramItem.getId(), 1248044836665479424L);
    }

    @Test
    public void testGetCaption() throws Exception {
        assertEquals(boatStagramItem.getCaption(), "Horizon... #veracruz #playa #sea #mar #boat #traveltheworld #travelling #mexico #mexico_maravilloso #blue #pinetree #travelgram #ontheroad #landscape #horizon #faraway #travelgram");
    }

    @Test
    public void testGetThumbnailUrl() throws Exception {
        assertEquals(boatStagramItem.getThumbnailUrl(), "https://scontent-fra3-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/13167244_109297296152737_2019378246_n.jpg?ig_cache_key=MTI0ODA0NDgzNjY2NTQ3OTU0Mg%3D%3D.2");
    }

    @Test
    public void testGetDisplayImageUrl() throws Exception {
        assertEquals(boatStagramItem.getDisplayImageUrl(), "https://scontent-fra3-1.cdninstagram.com/t51.2885-15/e35/13167244_109297296152737_2019378246_n.jpg?ig_cache_key=MTI0ODA0NDgzNjY2NTQ3OTU0Mg%3D%3D.2");
    }

    @Test
    public void testGetHeight() throws Exception {
        assertEquals(boatStagramItem.getHeight(), 1080);
    }
}
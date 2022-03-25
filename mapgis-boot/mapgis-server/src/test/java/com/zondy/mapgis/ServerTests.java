package com.zondy.mapgis;

import com.zondy.mapgis.modules.monitor.controller.domain.Server;
import org.junit.jupiter.api.Test;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class ServerTests {
    @Test
    public void testGetServerInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
    }
}

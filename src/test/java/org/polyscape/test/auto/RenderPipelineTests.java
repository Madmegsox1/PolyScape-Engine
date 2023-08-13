package org.polyscape.test.auto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.polyscape.rendering.Display;

/**
 * @author Madmegsox1
 * @since 13/08/2023
 */

public class RenderPipelineTests {
    @Test
    public void initDisplayTest(){
        Display display = new Display("TEST");
        Assertions.assertNotNull(display);
        display.init(true);
        Assertions.assertNotEquals(display.getWindow(), 0L);
    }
}



package org.openslide;

public class OpenSlideDisposedException extends RuntimeException {
    private static final String MSG = "OpenSlide object has been disposed";

    public OpenSlideDisposedException() {
        super(MSG);
    }
}

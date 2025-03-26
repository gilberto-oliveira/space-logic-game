package br.edu.unifesspa.mc.logicgame.framework.image.loader;

import org.w3c.dom.Element;

import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItemList;

/**
 * Each <code>ImageLoader</code> is capable of loading image types based on a
 * string.
 *
 * @author Vin�cius
 */
public interface ImageLoader {

    /**
     * Tag that this loadware deals with.
     *
     * @return The tag that this loadware deals with.
     */
    public String getTagName();

    public ImageItemList<ImageItem> load(Element param);
}

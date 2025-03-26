package br.edu.unifesspa.mc.logicgame.framework.image.loader;

import org.w3c.dom.Element;

import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItemList;
import br.edu.unifesspa.mc.logicgame.framework.image.ImagePallete;

/**
 * Loads a single image from a file. Understands the following XML element:
 * <p>
 *
 * <pre>
 * &lt;single src=&quot;image1.jpg&quot; name=&quot;image1&quot;&gt;
 *    &lt;change width=&quot;100&quot; height=&quot;100&quot; rotate=&quot;0&quot; flip=&quot;BOTH&quot;&gt;
 * &lt;/single&gt;
 *
 * </pre>
 *
 * <p>
 * Where the change and name arguments are optional. If the name is ommited, the
 * image name without the extension will be used.
 *
 * @author Vin�cius
 */
public class SingleLoader extends AbstractLoader {

    public SingleLoader(ImagePallete pallete) {
        super(pallete);
    }

    public ImageItemList<ImageItem> load(Element param) {
        return new ImageItemList<ImageItem>(loadImageElement(param));
    }

    public String getTagName() {
        return "single";
    }
}

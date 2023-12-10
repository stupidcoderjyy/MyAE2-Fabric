package util;

import com.google.gson.JsonElement;
import com.stupidcoderx.modding.util.serialize.ArrayVal;
import com.stupidcoderx.modding.util.serialize.ContainerVal;
import org.junit.Assert;
import org.junit.Test;

public class SerializeTest {

    @Test
    public void test() {
        ContainerVal c = new ContainerVal();
        c.newContainer("c1").newInt("i").set(15);
        c.newContainer("c2").newContainer("cc").newInt("k").set(25);
        ArrayVal arr = c.newArray("arr");
        arr.addString().set("test");
        arr.addInt().set(2);
        arr.addInt().set(5);

        JsonElement json = c.toJson(null);
        System.out.println(json);
        c = new ContainerVal().fromJson(json);

        Assert.assertEquals((Integer) 15, c.get("c1").asContainer().get("i").asInt().get());
        Assert.assertEquals((Integer) 25, c.get("c2").asContainer().get("cc").asContainer().get("k").asInt().get());
        Assert.assertEquals((Integer) 5, c.get("arr").asArray().get(2).asInt().get());
    }
}

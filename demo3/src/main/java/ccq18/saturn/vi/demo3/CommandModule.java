package ccq18.saturn.vi.demo3;

import com.google.inject.Binder;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import static com.google.inject.name.Names.named;

@Slf4j
public class CommandModule implements Module {


    @Override
    @SuppressWarnings("unchecked")
    public void configure(Binder binder) {
        Properties p = new Properties();
        try {
            InputStream is = CommandModule.class.getClassLoader().getResourceAsStream("app.properties");
            p.load(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
        Enumeration e = p.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = (String) p.get(key);
            binder.bindConstant().annotatedWith(named(key)).to(value);
        }
    }


}
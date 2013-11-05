package jhc.figaro.api.resource.framework;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Before;

public abstract class AbstractMashallingTest {

    protected final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
        AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
        AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
        mapper.setAnnotationIntrospector(pair);
    }

    protected String writeJson(Object object) throws Exception {

        Writer writer = new StringWriter();
        mapper.writeValue(writer, object);
        return writer.toString();
    }

    protected <T> T readJson(String jsonString, Class<? extends T> valueType) throws Exception {
        T jsonObject = mapper.readValue(jsonString, valueType);
        return jsonObject;
    }

    protected String writeXml(Object resource) throws Exception {
        Writer writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(resource.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(resource, writer);
        return writer.toString();
    }

}

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

// Reads and writes the catalog in JSON with Jackson
public class JsonRepository {

    private final File file;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonRepository(String fileName) {
        file = new File(fileName);
    }

    public boolean exists() {
        return file.exists();
    }

    // Deserialization: JSON -> Catalogo
    public Catalogo load() throws IOException {
        return mapper.readValue(file, Catalogo.class);
    }

    // Serialization: Catalogo -> JSON, indented
    public void save(Catalogo catalogo) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, catalogo);
    }
}
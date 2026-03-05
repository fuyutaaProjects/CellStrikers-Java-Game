package src.main.java.com.views.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import src.libs.json.JSONArray;
import src.libs.json.JSONObject;
import src.main.java.com.models.CellType;
import src.main.java.com.models.Direction;
import src.main.java.com.views.Texture;

public class CellTextureLoader {

    public static Texture[] loadTextures(String path) {
        File file = new File(path);

        if (!file.exists()) {
            System.err.println("Texture JSON file not found at: " + file.getAbsolutePath());
            return new Texture[0];
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);
            JSONArray texturesArray = json.getJSONArray("textures");

            ArrayList<Texture> textureList = new ArrayList<>();

            for (int i = 0; i < texturesArray.length(); i++) {
                JSONObject tex = texturesArray.getJSONObject(i);

                String name = tex.isNull("name") ? null : tex.getString("name");
                String cellTypeStr = tex.isNull("cellType") ? null : tex.getString("cellType");
                String directionStr = tex.isNull("direction") ? null : tex.getString("direction");

                CellType cellType = cellTypeStr != null ? CellType.valueOf(cellTypeStr) : null;
                Direction direction = directionStr != null ? Direction.valueOf(directionStr) : null;

                String smallTexture = tex.getString("smallTexture");

                Texture texture;

                if (tex.has("bigTextureMulti")) {
                    JSONArray bigMultiArray = tex.getJSONArray("bigTextureMulti");
                    String[][] bigTextureMulti = new String[bigMultiArray.length()][];

                    for (int j = 0; j < bigMultiArray.length(); j++) {
                        JSONArray inner = bigMultiArray.getJSONArray(j);
                        String[] line = new String[inner.length()];
                        for (int k = 0; k < inner.length(); k++) {
                            line[k] = inner.getString(k);
                        }
                        bigTextureMulti[j] = line;
                    }

                    texture = new Texture(name, cellType, direction, smallTexture, bigTextureMulti);

                } else {
                    JSONArray bigArray = tex.getJSONArray("bigTexture");
                    String[] bigTexture = new String[bigArray.length()];
                    for (int j = 0; j < bigArray.length(); j++) {
                        bigTexture[j] = bigArray.getString(j);
                    }

                    texture = new Texture(name, cellType, direction, smallTexture, bigTexture);
                }

                textureList.add(texture);
            }

            return textureList.toArray(new Texture[0]);

        } catch (IOException e) {
            e.printStackTrace();
            return new Texture[0];
        }
    }
}

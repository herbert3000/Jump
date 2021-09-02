package game;

import litecom.inca.Point3d;

public class SpriteHolder {

	public static final int nSprites = 9;

    public static Sprite getSprite(int i, Point3d point3d) {
        switch(i) {
        case 0:
            return new ChocoladeSprite(point3d);

        case 1:
            return new DonutSprite(point3d);

        case 2:
            return new MedkitSprite(point3d);

        case 3:
            return new KeySprite(point3d, 0);

        case 4:
            return new KeySprite(point3d, 1);

        case 5:
            return new KeySprite(point3d, 2);

        case 6:
            return new KeySprite(point3d, 3);

        case 7:
            return new CrystalSprite(point3d);

        case 8:
            return new ExtraLifeSprite(point3d);
        }
        return null;
    }

    public static byte getIndex(Sprite sprite) {
        if (sprite instanceof ChocoladeSprite)
            return 0;
        if (sprite instanceof DonutSprite)
            return 1;
        if (sprite instanceof MedkitSprite)
            return 2;
        if (sprite instanceof KeySprite)
            switch(((KeySprite)sprite).key) {
            case 0:
                return 3;

            case 1:
                return 4;

            case 2:
                return 5;

            case 3:
                return 6;
            }
        if (sprite instanceof CrystalSprite)
            return 7;
        return ((byte)(!(sprite instanceof ExtraLifeSprite) ? -1 : 8));
    }

    public static String getName(int i) {
        switch(i) {
        case 0:
            return "Chocolade";

        case 1:
            return "Donut";

        case 2:
            return "Medkit";

        case 3:
            return "Gold key";

        case 4:
            return "Silver key";

        case 5:
            return "Blue key";

        case 6:
            return "Red key";

        case 7:
            return "Crystal";

        case 8:
            return "Extra Life";
        }
        return "Unknown.";
    }
}

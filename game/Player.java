package game;

import java.util.Vector;
import litecom.Debug;
import litecom.inca.*;
//import litecom.sound.SoundManager;

public class Player {

    public static final double angleLimit = 1.2707963267948965D;
    public static final double squishDamage = 20D;
    public static final int MIDAIR = 1;
    public static final int ON_GROUND = 2;
    public static final int nKeys = 4;
    public static final double xlen = 30D;
    public static final double ylen = 60D;
    public static final double zlen = 30D;
    public static final double maxStairSize = 35D;
    public static final double jumpStrength = 300D;
    public static double jumpFactor = 3000D;
    public Point3d pos;
    public Point3d delta;
    public Point3d realDelta;
    public Point3d dir;
    public Point3d fakeDynamicDelta;
    public Shape3d gubbe;
    public Shape3d hjul1;
    public Shape3d hjul2;
    private Geometry gubbeSource;
    public int currLevel;
    public int lives;
    public boolean key[];
    public double mouseFactor;
    public double speed;
    public double rotSpeed;
    public double udSpeed;
    public double life;
    public boolean keyForward;
    public boolean keyBackward;
    public boolean keyLeft;
    public boolean keyRight;
    public boolean keyJump;
    public boolean keyUp;
    public boolean keyDown;
    public boolean visible;
    public boolean collidedX;
    public boolean collidedY;
    public boolean collidedZ;
    public boolean holdingDownJump;
    public boolean dynHit;
    public boolean killed;
    public boolean beeingSquished;
    public int state;
    public long jumpStartedAt;
    public double smoothY;
    private int points;
    public boolean cheater;
    private int lastPoints;
    double realSmoothY;
    double deltaFlum;
    Vector<Platform> platformOnPlayer;
    double lastDir;

    public Player() {
        delta = new Point3d();
        realDelta = new Point3d();
        dir = new Point3d();
        fakeDynamicDelta = new Point3d();
        currLevel = 1;
        lives = 2;
        key = new boolean[4];
        mouseFactor = 1.0D;
        life = 100D;
        jumpStartedAt = -1L;
        cheater = false;
        lastPoints = -1;
        Debug.out(this, "<init>");
        delta = new Point3d();
        gubbe = core.shapeHolder.getShape(0);
        hjul1 = core.shapeHolder.getShape(1);
        hjul2 = core.shapeHolder.getShape(1);
        gubbeSource = new Geometry(gubbe.geometry.source.length);
        for (int i = 0; i < gubbe.geometry.source.length; i++) {
            gubbeSource.source[i].x = gubbe.geometry.source[i].x;
            gubbeSource.source[i].y = gubbe.geometry.source[i].y;
            gubbeSource.source[i].z = gubbe.geometry.source[i].z;
        }
        gubbeSource.copyDest();
    }

    public void setPosRot(Point3d mb1, Point3d mb2)
    {
        pos = mb1;
        dir = mb2.neg();
        dir.y += Math.PI / 2;
        delta = new Point3d();
    }

    public void clearKeys() {
        for (int i = 0; i < 4; i++)
            key[i] = false;
    }

    public synchronized void update(double d) {
        updateDelta(d);
        beeingSquished = false;
        updateDynamicCollision(d);
        updateStaticCollision(d);
        updateSpeedAndHeading(d);
        if (killed)
            return;
        updateSpriteCollision(d);
        updateSmoothY(d);
        updateAvatar(d);
        updateSensors();
        if (pos.y < -1500D) {
            giveDamage(100D);
            core.soundManager.play("fall");
        }
    }

    public void giveDamage(double d) {
        if (core.world.flag[111])
            return;
        life -= d;
        if (life <= 0.0D) {
            Debug.out("AAARGH! IM DEAD!");
            killed = true;
        }
    }

    public void resetPoints() {
        points = 0;
        lastPoints = 0;
    }

    public int getPoints() {
        return points / 3;
    }

    public void addPoints(int i) {
        if (cheater)
            return;
        
        points += i * 3;
        lastPoints = points / 2;
    }

    public void checkScores() {
        if (lastPoints != -1 && lastPoints * 2 != points) {
            points = 0;
            throw new RuntimeException("blah");
        }
    }

    private void updateSpriteCollision(double d) {
        Segment segment = core.world.getSegment(pos);
        if (segment != null && segment.sprite != null) {
            if (segment.sprite instanceof ChocoladeSprite) {
                addPoints(100);
                if (life < 100D)
                    if (life < 98D)
                        life += 2D;
                    else
                        life = 100D;
                core.soundManager.play("pling1");
            } else if (segment.sprite instanceof DonutSprite) {
                addPoints(5000);
                if (life < 100D)
                    if (life < 94D)
                        life += 6D;
                    else
                        life = 100D;
                core.soundManager.play("pling3");
            } else if (segment.sprite instanceof KeySprite) {
                addPoints(500);
                Debug.out("Got key!");
                key[((KeySprite)segment.sprite).key] = true;
                core.soundManager.play("pling3");
            } else if(segment.sprite instanceof MedkitSprite) {
                addPoints(400);
                if (life < 100D)
                    if (life < 30D)
                        life += 70D;
                    else
                        life = 100D;
                core.soundManager.play("pling3");
            } else if (segment.sprite instanceof CrystalSprite) {
                addPoints(500);
                Debug.out("Ending level.");
                core.world.flag[111] = true;
            } else if(segment.sprite instanceof ExtraLifeSprite) {
                addPoints(1000);
                lives++;
                Debug.out("Extra life!");
                core.gameLayer.setMessage(Locale.GLPExtraLife);
            }
            segment.sprite.removeSprite();
        }
    }

    private void updateSmoothY(double d) {
        realSmoothY += (pos.y - realSmoothY) / (0.2D / d);
        if (Math.abs(realSmoothY - pos.y) < 0.1D)
            realSmoothY = pos.y;
        if (realSmoothY > pos.y)
            realSmoothY = pos.y;
        deltaFlum += (delta.y - deltaFlum) / (0.2D / d);
        smoothY = realSmoothY + deltaFlum / 20D;
        if (smoothY > pos.y)
            smoothY = pos.y;
        if (smoothY < pos.y - 40D)
            smoothY = pos.y - 40D;
    }

    private void squish() {
        Segment segment = core.world.getSegment(pos);
        Point3d point3d = new Point3d(segment.segPos.x * 100D + 50D, segment.segPos.y * 100D + 50D, segment.segPos.z * 100D + 50D);
        Point3d point3d1 = new Point3d(pos);
        pos.x += point3d.x - pos.x;
        pos.z += point3d.z - pos.z;
        Platform platform = core.world.getPlatform(pos.x + 30D, pos.y - 60D, pos.z + 30D);
        if (platform == null)
            platform = core.world.getPlatform(pos.x - 30D, pos.y - 60D, pos.z + 30D);
        if (platform == null)
            platform = core.world.getPlatform(pos.x - 30D, pos.y - 60D, pos.z - 30D);
        if (platform == null)
            platform = core.world.getPlatform(pos.x + 30D, pos.y - 60D, pos.z - 30D);
        if (platform != null) {
            pos.y = platform.pos.y + 50D + 60D + 1.0D;
        } else {
            Platform platform1 = core.world.getPlatform(pos.x + 30D, pos.y + 60D, pos.z + 30D);
            if (platform1 == null)
                platform1 = core.world.getPlatform(pos.x - 30D, pos.y + 60D, pos.z + 30D);
            if (platform1 == null)
                platform1 = core.world.getPlatform(pos.x - 30D, pos.y + 60D, pos.z - 30D);
            if (platform1 == null)
                platform1 = core.world.getPlatform(pos.x + 30D, pos.y + 60D, pos.z - 30D);
            if (platform1 != null)
                pos.y = platform1.pos.y - 50D - 60D - 1.0D;
        }
        point3d1.sub(pos);
        double d = (point3d1.dist(new Point3d()) - 25D) / 10D;
        if(d < 0.1D)
            d = 0.3D;
        giveDamage(20D * d);
        core.soundManager.play("squish");
        beeingSquished = true;
    }

    private boolean tryStair(double d, double d1, double d2, Point3d p1, Point3d p2, Point3d p3) {
        if (!core.world.isSolid(p2.x + p1.x + 30D, (p2.y - 60D) + 35D, p2.z + 30D + p1.z) && !core.world.isSolid((p2.x + p1.x) - 30D, (p2.y - 60D) + 35D, p2.z + 30D + p1.z) && !core.world.isSolid(p2.x + p1.x + 30D, (p2.y - 60D) + 35D, (p2.z - 30D) + p1.z) && !core.world.isSolid((p2.x + p1.x) - 30D, (p2.y - 60D) + 35D, (p2.z - 30D) + p1.z)) {
            Platform platform = core.world.getPlatform(d + p1.x, d1, d2 + p1.z);
            if (platform == null) {
                Debug.out(this, "WARNING! problem in collision routine(stair check).");
                return false;
            }
            double d3 = ((platform.pos.y + 50D) - d1) + 1.0D;
            if (!core.world.isSolid(p2.x + p1.x + 30D, p2.y + 60D + d3, p2.z + 30D + p1.z) && !core.world.isSolid((p2.x + p1.x) - 30D, p2.y + 60D + d3, p2.z + 30D + p1.z) && !core.world.isSolid(p2.x + p1.x + 30D, p2.y + 60D + d3, (p2.z - 30D) + p1.z) && !core.world.isSolid((p2.x + p1.x) - 30D, p2.y + 60D + d3, (p2.z - 30D) + p1.z)) {
                p2.y += d3;
                state = 2;
                p3.y = 0.0D;
                delta.y = 0.0D;
                collidedY = true;
                return true;
            }
        }
        return false;
    }

    private void checkPlayerCollision(double x, double y, double z, Point3d p1, Point3d p2, boolean flag) {
        if (p1.isZero() || beeingSquished)
            return;
        if (core.world.isSolid(x, y, z)) {
            p2.sub(p1);
            x -= p1.x;
            y -= p1.y;
            z -= p1.z;
            if (core.world.isSolid(x, y, z)) {
                squish();
                return;
            }
            if (core.world.isSolid(x + p1.x, y, z)) {
                boolean flag1 = false;
                if (flag)
                    flag1 = tryStair(x, y, z, new Point3d(p1.x, 0.0D, 0.0D), p2, p1);
                if (!flag1) {
                    p1.x = 0.0D;
                    collidedX = true;
                    return;
                }
            } else if (core.world.isSolid(x, y, z + p1.z)) {
                boolean flag2 = false;
                if (flag)
                    flag2 = tryStair(x, y, z, new Point3d(0.0D, 0.0D, p1.z), p2, p1);
                if (!flag2) {
                    p1.z = 0.0D;
                    collidedZ = true;
                    return;
                }
            } else {
                Platform platform = core.world.getPlatform(x + p1.x, y + p1.y, z + p1.z);
                if (platform == null)
                    Debug.out("buttfuck.");
                if (p1.y < 0.0D) {
                    state = 2;
                    if (Math.abs(delta.y - platform.del.y) > 400D) {
                        core.soundManager.play("hopp-ner");
                        Debug.out("player.delta.y=" + delta.y + ", platform.delta.y=" + platform.del.y);
                    }
                    checkLanding(Math.abs(delta.y - platform.del.y));
                    if(delta.y < platform.del.y)
                        delta.y = platform.del.y;
                }
                if (p1.y > 0.0D && delta.y > platform.del.y)
                    delta.y = platform.del.y;
                fakeDynamicDelta.x = platform.del.x;
                fakeDynamicDelta.z = platform.del.z;
                p1.y = 0.0D;
                collidedY = true;
            }
        }
    }

    private void updateSensors() {
        Point3d point3d = new Point3d(pos.x, pos.y, pos.z);
        Segment segment = core.world.getSegment(point3d);
        byte byte0 = -1;
        if (segment != null && segment.sensor != null && (!segment.sensor.button || segment.sensor.button && pos.y % 100D < 75D))
            if (segment.sensor.key != -1 && !key[segment.sensor.key]) {
                switch(segment.sensor.key) {
                case 0:
                    core.gameLayer.setMessage(Locale.PNeedGoldKey);
                    break;

                case 1:
                    core.gameLayer.setMessage(Locale.PNeedSilverKey);
                    break;

                case 2:
                    core.gameLayer.setMessage(Locale.PNeedBlueKey);
                    break;

                case 3:
                    core.gameLayer.setMessage(Locale.PNeedRedKey);
                    break;
                }
            } else {
                if (!core.world.flag[segment.sensor.destFlag])
                    Debug.out("Setting flag: " + segment.sensor.destFlag);
                core.world.flag[segment.sensor.destFlag] = true;
                byte0 = segment.sensor.destFlag;
            }
        for (int i = 0; i < World.maxFlags; i++)
            if(i != byte0 && i != 111)
                core.world.flag[i] = false;
    }

    private void checkLanding(double d) {
        if (d < 1000D)
            return;
        Debug.out("Landing, strength: " + d);
        giveDamage((d - 1000D) / 5D);
        if (d > 1100D)
            giveDamage(60D);
        core.soundManager.play("hopp-ner-hard");
    }

    private void checkPlatformCollision(double x, double y, double z) {
        Platform platform = core.world.getPlatform(x, y, z);
        if (platform != null)
            platformOnPlayer.addElement(platform);
    }

    private void updateDelta(double d) {
        if (state == 2) {
            delta.x = -Math.cos(dir.y - Math.PI / 2) * speed + fakeDynamicDelta.x;
            delta.z = -Math.sin(dir.y - Math.PI / 2) * speed + fakeDynamicDelta.z;
        }
        if (jumpStartedAt == -1L && keyJump && state == 2 && !holdingDownJump) {
            core.soundManager.play("hopp-upp");
            jumpStartedAt = System.currentTimeMillis();
            holdingDownJump = true;
            delta.y += 300D;
        }
        delta.y += World.gravity * d;
        if (jumpStartedAt != -1L) {
            delta.y -= World.gravity * d;
            if (!keyJump) {
                jumpStartedAt = -1L;
            } else {
                double d1 = (double)(System.currentTimeMillis() - jumpStartedAt) / 1000D;
                if(d1 >= 0.3D)
                    jumpStartedAt = -1L;
            }
        }
        realDelta.x = delta.x;
        realDelta.y = delta.y;
        realDelta.z = delta.z;
        realDelta.mul(d);
    }

    private void updateAvatar(double d) {
        double d1 = (dir.y - lastDir) * 2D;
        Point3d point3d = new Point3d(0.0D, 0.0D, (speed * d) / 60D + d1);
        for (int i = 0; i < hjul1.geometry.source.length; i++)
            hjul1.geometry.source[i].rotate(point3d);

        for (int i = 0; i < hjul1.normal.source.length; i++)
            hjul1.normal.source[i].rotate(point3d);

        point3d = new Point3d(0.0D, 0.0D, (speed * d) / 60D - d1);
        for (int i = 0; i < hjul2.geometry.source.length; i++)
            hjul2.geometry.source[i].rotate(point3d);

        for (int i = 0; i < hjul2.normal.source.length; i++)
            hjul2.normal.source[i].rotate(point3d);

        for (int i = 0; i < gubbeSource.source.length; i++)
            gubbeSource.source[i].rotate(new Point3d(0.0D, 0.0D, speed / 650D), gubbe.geometry.source[i]);

        hjul1.rotation.y = -dir.y + Math.PI / 2;
        hjul2.rotation.y = -dir.y + Math.PI / 2;
        gubbe.rotation.y = -dir.y + Math.PI / 2;
        gubbe.position.x = pos.x;
        gubbe.position.y = smoothY;
        gubbe.position.z = pos.z;
        hjul1.position.x = pos.x + Math.sin(hjul1.rotation.y) * 27D;
        hjul1.position.y = pos.y - 30D;
        hjul1.position.z = pos.z + Math.cos(hjul1.rotation.y) * 27D;
        hjul2.position.x = pos.x + Math.sin(hjul2.rotation.y + Math.PI) * 27D;
        hjul2.position.y = pos.y - 30D;
        hjul2.position.z = pos.z + Math.cos(hjul2.rotation.y + Math.PI) * 27D;
        lastDir = dir.y;
    }

    private void updateDynamicCollision(double d) {
        platformOnPlayer = new Vector<Platform>(5);
        checkPlatformCollision(pos.x + 30D, pos.y + 60D, pos.z + 30D);
        checkPlatformCollision(pos.x - 30D, pos.y + 60D, pos.z + 30D);
        checkPlatformCollision(pos.x + 30D, pos.y + 60D, pos.z - 30D);
        checkPlatformCollision(pos.x - 30D, pos.y + 60D, pos.z - 30D);
        checkPlatformCollision(pos.x + 30D, pos.y, pos.z + 30D);
        checkPlatformCollision(pos.x - 30D, pos.y, pos.z + 30D);
        checkPlatformCollision(pos.x + 30D, pos.y, pos.z - 30D);
        checkPlatformCollision(pos.x - 30D, pos.y, pos.z - 30D);
        checkPlatformCollision(pos.x + 30D, pos.y - 60D, pos.z + 30D);
        checkPlatformCollision(pos.x - 30D, pos.y - 60D, pos.z + 30D);
        checkPlatformCollision(pos.x + 30D, pos.y - 60D, pos.z - 30D);
        checkPlatformCollision(pos.x - 30D, pos.y - 60D, pos.z - 30D);
        state = 1;
        dynHit = false;
        if (platformOnPlayer.size() != 0) {
            dynHit = true;
            for (int l = 0; l < platformOnPlayer.size(); l++) {
                Platform platform = (Platform)platformOnPlayer.elementAt(l);
                if(platform.owner == null || platform.del.isZero()) {
                    Debug.out("2");
                    squish();
                    return;
                }
                if (platform.del.y > 0.0D) {
                    state = 2;
                    if (delta.y < platform.del.y) {
                        delta.y = platform.del.y;
                        checkLanding(Math.abs(delta.y - platform.del.y));
                    }
                    double d1 = platform.pos.y + 50D + 60D + 10D * d;
                    if (d1 > pos.y)
                        pos.y = d1;
                } else if (platform.del.y < 0.0D) {
                    if (delta.y > platform.del.y)
                        delta.y = platform.del.y;
                    pos.y = platform.pos.y - 50D - 60D - 10D * d;
                } else if (platform.del.x > 0.0D)
                    pos.x = platform.pos.x + 50D + 30D + 10D * d;
                else if (platform.del.x < 0.0D)
                    pos.x = platform.pos.x - 50D - 30D - 10D * d;
                else if (platform.del.z > 0.0D)
                    pos.z = platform.pos.z + 50D + 30D + 10D * d;
                else
                if (platform.del.z < 0.0D)
                    pos.z = platform.pos.z - 50D - 30D - 10D * d;
                else
                    Debug.out("w2134ow - " + platform.del);
            }
        }
    }

    private void updateStaticCollision(double d) {
        if (beeingSquished)
            return;
        collidedX = false;
        collidedY = false;
        collidedZ = false;
        if (core.world.isSolid(pos.x, pos.y - 60D, pos.z)) {
            Debug.out("3");
            squish();
            return;
        }
        double d1 = Math.round(500D * d);
        Point3d point3d = new Point3d(realDelta);
        point3d.div(d1);
        for (int i = 0; i < (int)d1; i++) {
            if (point3d.isZero())
                break;
            pos.add(point3d);
            checkPlayerCollision(pos.x + 30D, pos.y - 60D, pos.z + 30D, point3d, pos, true);
            checkPlayerCollision(pos.x + 30D, pos.y - 60D, pos.z - 30D, point3d, pos, true);
            checkPlayerCollision(pos.x - 30D, pos.y - 60D, pos.z + 30D, point3d, pos, true);
            checkPlayerCollision(pos.x - 30D, pos.y - 60D, pos.z - 30D, point3d, pos, true);
            checkPlayerCollision(pos.x + 30D, pos.y, pos.z + 30D, point3d, pos, false);
            checkPlayerCollision(pos.x + 30D, pos.y, pos.z - 30D, point3d, pos, false);
            checkPlayerCollision(pos.x - 30D, pos.y, pos.z + 30D, point3d, pos, false);
            checkPlayerCollision(pos.x - 30D, pos.y, pos.z - 30D, point3d, pos, false);
            checkPlayerCollision(pos.x + 30D, pos.y + 60D, pos.z + 30D, point3d, pos, false);
            checkPlayerCollision(pos.x + 30D, pos.y + 60D, pos.z - 30D, point3d, pos, false);
            checkPlayerCollision(pos.x - 30D, pos.y + 60D, pos.z + 30D, point3d, pos, false);
            checkPlayerCollision(pos.x - 30D, pos.y + 60D, pos.z - 30D, point3d, pos, false);
            if (beeingSquished)
                return;
        }

        if (core.world.isSolid(pos.x, pos.y - 60D, pos.z)) {
            Debug.out("4");
            squish();
        }
    }

    private void updateSpeedAndHeading(double d) {
        if (keyForward)
            speed += 800D * d;
        else if (keyBackward)
            speed -= 800D * d;
        else
            speed *= Math.pow(0.01D, d);
        
        if (keyLeft)
            rotSpeed += d * 10D;
        else if (keyRight)
            rotSpeed -= d * 10D;
        else
            rotSpeed *= Math.pow(0.0001D, d);
        
        if (keyUp)
            udSpeed += d * 10D * mouseFactor;
        else if (keyDown)
            udSpeed -= d * 10D * mouseFactor;
        else
            udSpeed *= Math.pow(0.0001D, d);
        
        if (rotSpeed > 3D)
            rotSpeed = 3D;
        if (rotSpeed < -3D)
            rotSpeed = -3D;
        if (udSpeed > 3D)
            udSpeed = 3D;
        if (udSpeed < -3D)
            udSpeed = -3D;
        if (speed > 350D)
            speed = 350D;
        if (speed < -170D)
            speed = -170D;
        if (Math.abs(speed) < 0.1D)
            speed = 0.0D;
        dir.y += rotSpeed * d;
        dir.x += udSpeed * d;
        if (dir.x > angleLimit) {
            dir.x = angleLimit;
            udSpeed = 0.0D;
        }
        if (dir.x < -angleLimit) {
            dir.x = -angleLimit;
            udSpeed = 0.0D;
        }
    }

    public synchronized void hidePlayer() {
        if (!visible)
            return;

        core.vWorld.removeShape(hjul1);
        core.vWorld.removeShape(hjul2);
        core.vWorld.removeShape(gubbe);
        visible = false;
    }

    public synchronized void showPlayer() {
        if (visible)
            return;

        core.vWorld.addShape(hjul1);
        core.vWorld.addShape(hjul2);
        core.vWorld.addShape(gubbe);
        visible = true;
    }

    public synchronized void reverseMouse() {
        mouseFactor *= -1D;
    }

    public synchronized void setForward(boolean flag) {
        keyForward = flag;
    }

    public synchronized void setBackward(boolean flag) {
        keyBackward = flag;
    }

    public synchronized void setLeft(boolean flag) {
        keyLeft = flag;
    }

    public synchronized void setRight(boolean flag) {
        keyRight = flag;
    }

    public synchronized void setUp(boolean flag) {
        keyUp = flag;
    }

    public synchronized void setDown(boolean flag) {
        keyDown = flag;
    }

    public synchronized void setJump(boolean flag) {
        keyJump = flag;
        if (!flag)
            holdingDownJump = false;
    }

    public void remove() {
        hidePlayer();
    }
}

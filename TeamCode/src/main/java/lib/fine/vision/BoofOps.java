package lib.fine.vision;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import boofcv.android.ConvertBitmap;
import boofcv.io.calibration.CalibrationIO;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageBase;

/**
 * Created by drew on 1/31/18.
 */

public class BoofOps {

    public static void displayBitmap(final Bitmap bMap, final ImageView imageView) {

        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(bMap);
            }
        });
    }

    public static void displayBoof(final ImageBase boof, final ImageView imageView) {
        Bitmap bMap = Bitmap.createBitmap(boof.getWidth(), boof.getHeight(), Bitmap.Config.ARGB_8888);
        ConvertBitmap.boofToBitmap(boof, bMap, null);

        displayBitmap(bMap, imageView);
    }
    public static void displayCurrentFrame(VuforiaLocalizer vuforiaLocalizer, ImageView imageView) throws InterruptedException {
        displayBitmap(getFrameAsBitmap(vuforiaLocalizer),imageView);
    }
    public static GrayU8 getFrame(VuforiaLocalizer vuforia) throws InterruptedException {
        Bitmap bm = getFrameAsBitmap(vuforia);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bm =  Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        GrayU8 imag = ConvertBitmap.bitmapToGray(bm, null, GrayU8.class, null);

        return imag;
    }

    public static Bitmap getFrameAsBitmap(VuforiaLocalizer vuforia) throws InterruptedException {
        VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take(); //takes the frame at the head of the queue
        Image rgb = null;

        long numImages = frame.getNumImages();

        for (int i = 0; i < numImages; i++) {
            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                rgb = frame.getImage(i);
                break;
            }
        }
        Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(rgb.getPixels());

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bm =  Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        return bm;
    }
    public static CameraPinholeRadial loadIntrinsic(LinearOpMode opMode) {
        CameraPinholeRadial intrinsic = null;
        //String user = System.getProperty("user.dir");
        //Log.e("DemooMain", System.getProperty("user.dir"));


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + "FIRST"
                + "/boofImages/config.txt");


        try {
            FileInputStream fos = new FileInputStream(mediaStorageDir);
            Reader reader = new InputStreamReader(fos);
            intrinsic = CalibrationIO.load(reader);
            opMode.telemetry.addData("Got?", "yes");
        } catch (RuntimeException e) {
            Log.w("DemoMain", "Failed to load intrinsic parameters: "+e.getClass().getSimpleName());
            e.printStackTrace();
            opMode.telemetry.addData("Got?", "no");

        } catch (FileNotFoundException ignore) {
            opMode.telemetry.addData("Got?", "no");

        }
        opMode.telemetry.update();

        return intrinsic;//new CameraPinholeRadial();
    }
}

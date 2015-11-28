package de.geithonline.systemlwp.settings;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerAokpCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerAokpCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerAokpCircleV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBarGraphVerticalV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBatteryV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerBrickV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerColorCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerFlowerV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerFlowerV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerNumberOnlyV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleArcV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleArcV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleArcV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV6;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV7;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV8;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerSimpleCircleV9;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerStarV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerStarV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerStarV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerStarV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerTachoV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaCircleV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV1;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV2;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV3;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV4;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV5;
import de.geithonline.systemlwp.bitmapdrawer.BitmapDrawerZoopaWideV6;
import de.geithonline.systemlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerAsymetricV1;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV1;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV2;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV3;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV4;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV5;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerClockV6;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerNewSimpleCircleV1;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerNewTachoV1;
import de.geithonline.systemlwp.bitmapdrawer.advanced.BitmapDrawerNewTachoV3;
import de.geithonline.systemlwp.bitmapdrawer.enums.TimerType;

public class DrawerManager {
	private static Map<String, IBitmapDrawer> drawer = new HashMap<String, IBitmapDrawer>();
	private static Map<String, Bitmap> iconCache = new HashMap<String, Bitmap>();

	static {
		drawer.put("ZoopaWideV1", new BitmapDrawerZoopaWideV1());
		drawer.put("ZoopaWideV2", new BitmapDrawerZoopaWideV2());
		drawer.put("ZoopaWideV3", new BitmapDrawerZoopaWideV3());
		drawer.put("ZoopaWideV4", new BitmapDrawerZoopaWideV4());
		drawer.put("ZoopaWideV5", new BitmapDrawerZoopaWideV5());
		drawer.put("ZoopaWideV6", new BitmapDrawerZoopaWideV6());
		drawer.put("ZoopaCircleV1", new BitmapDrawerZoopaCircleV1());
		drawer.put("ZoopaCircleV2", new BitmapDrawerZoopaCircleV2());
		drawer.put("ZoopaCircleV3", new BitmapDrawerZoopaCircleV3());
		drawer.put("TachoV1", new BitmapDrawerNewTachoV1());
		drawer.put("TachoV2", new BitmapDrawerTachoV2());
		drawer.put("TachoV3", new BitmapDrawerNewTachoV3());
		drawer.put("TachoV4", new BitmapDrawerTachoV4());
		drawer.put("TachoV5", new BitmapDrawerTachoV5());
		drawer.put("BrickV1", new BitmapDrawerBrickV1());
		drawer.put("BarGraphV1", new BitmapDrawerBarGraphV1());
		drawer.put("BarGraphV2", new BitmapDrawerBarGraphV2());
		drawer.put("BarGraphV3", new BitmapDrawerBarGraphV3());
		drawer.put("BarGraphV4", new BitmapDrawerBarGraphV4());
		drawer.put("BarGraphVerticalV1", new BitmapDrawerBarGraphVerticalV1());
		drawer.put("BarGraphVerticalV2", new BitmapDrawerBarGraphVerticalV2());
		drawer.put("BarGraphVerticalV3", new BitmapDrawerBarGraphVerticalV3());
		drawer.put("SimpleCircleV1", new BitmapDrawerSimpleCircleV1());
		drawer.put("SimpleCircleV2", new BitmapDrawerSimpleCircleV2());
		drawer.put("SimpleCircleV3", new BitmapDrawerSimpleCircleV3());
		drawer.put("SimpleCircleV4", new BitmapDrawerSimpleCircleV4());
		drawer.put("SimpleCircleV5", new BitmapDrawerSimpleCircleV5());
		drawer.put("SimpleCircleV6", new BitmapDrawerSimpleCircleV6());
		drawer.put("SimpleCircleV7", new BitmapDrawerSimpleCircleV7());
		drawer.put("SimpleCircleV8", new BitmapDrawerSimpleCircleV8());
		drawer.put("SimpleCircleV9", new BitmapDrawerSimpleCircleV9());
		drawer.put("ColorCircleV1", new BitmapDrawerColorCircleV1());
		drawer.put("AokpCircleV1", new BitmapDrawerAokpCircleV1());
		drawer.put("AokpCircleV2", new BitmapDrawerAokpCircleV2());
		drawer.put("AokpCircleV3", new BitmapDrawerAokpCircleV3());
		drawer.put("NumberOnlyV1", new BitmapDrawerNumberOnlyV1());
		drawer.put("StarV1", new BitmapDrawerStarV1());
		drawer.put("StarV2", new BitmapDrawerStarV2());
		drawer.put("StarV3", new BitmapDrawerStarV3());
		drawer.put("StarV4", new BitmapDrawerStarV4());
		drawer.put("FlowerV1", new BitmapDrawerFlowerV1());
		drawer.put("FlowerV2", new BitmapDrawerFlowerV2());
		drawer.put("BatteryV1", new BitmapDrawerBatteryV1());
		drawer.put("SimpleArcV1", new BitmapDrawerSimpleArcV1());
		drawer.put("SimpleArcV2", new BitmapDrawerSimpleArcV2());
		drawer.put("SimpleArcV3", new BitmapDrawerSimpleArcV3());
		drawer.put("ClockV1", new BitmapDrawerClockV1(TimerType.Without));
		drawer.put("ClockV1 (Timer Style)", new BitmapDrawerClockV1(TimerType.Timer));
		drawer.put("ClockV2", new BitmapDrawerClockV2(TimerType.Without));
		drawer.put("ClockV2 (Extra Level Bars)", new BitmapDrawerClockV2(TimerType.Timer));
		drawer.put("ClockV3", new BitmapDrawerClockV3());
		drawer.put("ClockV4", new BitmapDrawerClockV4());
		drawer.put("ClockV5", new BitmapDrawerClockV5());
		drawer.put("ClockV5 (Extra Level Bars)", new BitmapDrawerClockV5(TimerType.Timer));
		drawer.put("ClockV6", new BitmapDrawerClockV6());
		drawer.put("NewSimpleCircleV1", new BitmapDrawerNewSimpleCircleV1());
		drawer.put("NewSimpleCircleV1 (smaller)", new BitmapDrawerNewSimpleCircleV1(0.88f, 0.82f, 0.97f, 0.73f));
		drawer.put("AsymetricV1", new BitmapDrawerAsymetricV1());
	}

	public static IBitmapDrawer getDrawer(final String name) {
		IBitmapDrawer d = drawer.get(name);
		if (d == null) {
			d = drawer.get("ZoopaWideV1");
		}
		return d;
	}

	public static Bitmap getIconForDrawer(final String name, final int size) {
		Bitmap b = iconCache.get(name);
		if (b == null) {
			final IBitmapDrawer drawer = getDrawer(name);
			b = drawer.drawIcon(66, size);
			iconCache.put(name, b);
		}
		return b;
	}

}

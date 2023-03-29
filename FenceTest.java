package test;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;

import com.fasterxml.jackson.databind.JsonNode;
import com.yisuitech.entity.OptType;
import com.yisuitech.iot.entity.impl.GpsData;
//import com.yisuitech.iot.fence.FenceUtil;
//import com.yisuitech.iot.fence.IFenceCallback;
import com.yisuitech.iot.fence.*;
import com.yisuitech.iot.fence.analysis.CircleAnalysis;
import com.yisuitech.iot.fence.analysis.PolygonAnalysis;
import com.yisuitech.iot.fence.entity.DeviceLocation;
import com.yisuitech.iot.fence.entity.FenceBase;
import com.yisuitech.iot.fence.entity.FenceGeometry;
import com.yisuitech.iot.fence.util.PositionUtil;
import com.yisuitech.iot.util.GisUtil;
import com.yisuitech.util.JsonUtil;

public class FenceTest extends Thread{
	static private IFenceCallback fence;
	static private ArrayList<FenceGeometry> fences;
	static private ArrayList<DeviceLocation> devices;
	static private ArrayList<FenceGeometry> remainder;
	
	@Before
	public void regFence() {
		FenceCallbackImpl fenceCallback = new FenceCallbackImpl();
		
		fenceCallback.initAllFence();
		//FenceUtil.start(fenceCallback);
	}
	
	public static class Thread1 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
//			System.out.print("Calculation start\n");
	    	
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	int remainSize = remainder.size();
	    	
	    	for(int i = 0; i < fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
//	    		System.out.println(fence.getPolygon());
	    		
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					double distance = DistanceOp.distance(fence.getPolygon(), point);
//					System.out.println(distance);
				}
			}
	    	
	    	if(remainSize != 0) {
	    		// Analysis for remainders
		    	for(int i = 0; i < remainSize; i++) {
		    		FenceGeometry fence  = remainder.get(i);
//		    		System.out.println(fence.getPolygon());
		    		
					for(int j = 0; j < deviceSize; j++) {
						DeviceLocation device = devices.get(j);
						analysis.analysis(fence, device);
						
						GeometryFactory geometryFactory = new GeometryFactory();
						Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
						double distance = DistanceOp.distance(fence.getPolygon(), point);
						//System.out.println(distance);
					}
				}
	    	}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread1 time spent: %.4f ms\n", time);
	    }
	}
	
	public static class Thread2 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = fenceSize/8; i < 2*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					double distance = DistanceOp.distance(fence.getPolygon(), point);
//					System.out.println(distance);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread2 time spent: %.4f ms\n", time);
	    }
	}
	
	public static class Thread3 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 2*fenceSize/8; i < 3*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread3 time spent: %.4f ms\n", time);
	    }
	}
	
	public static class Thread4 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 3*fenceSize/8; i < 4*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread4 time spent: %.4f ms\n", time);
	    }
	}
	public static class Thread5 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 4*fenceSize/8; i < 5*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread5 time spent: %.4f ms\n", time);
	    }
	}
	public static class Thread6 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 5*fenceSize/8; i < 6*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread6 time spent: %.4f ms\n", time);
	    }
	}
	public static class Thread7 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 6*fenceSize/8; i < 7*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread7 time spent: %.4f ms\n", time);
	    }
	}
	public static class Thread8 implements Runnable {
	    @Override
	    public void run() {
	    	long startTime = System.nanoTime();
	    	PolygonAnalysis analysis = new PolygonAnalysis();
	    	int fenceSize = fences.size();
	    	int deviceSize = devices.size();
	    	
	    	for(int i = 7*fenceSize/8; i < 8*fenceSize/8; i++) {
	    		FenceGeometry fence  = fences.get(i);
				for(int j = 0; j < deviceSize; j++) {
					DeviceLocation device = devices.get(j);
					analysis.analysis(fence, device);
					
					GeometryFactory geometryFactory = new GeometryFactory();
					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
					DistanceOp.distance(fence.getPolygon(), point);
				}
			}
	    	
	    	long endTime = System.nanoTime();
	    	double time = (endTime - startTime) / 1000000.00;
	    	System.out.printf("Thread8 time spent: %.4f ms\n", time);
	    }
	}
//	public static class Thread9 implements Runnable {
//	    @Override
//	    public void run() {
//	    	long startTime = System.nanoTime();
//	    	PolygonAnalysis analysis = new PolygonAnalysis();
//	    	int fenceSize = fences.size();
//	    	int deviceSize = devices.size();
//	    	
//	    	for(int i = 8*fenceSize/8; i < 9*fenceSize/8; i++) {
//	    		FenceGeometry fence  = fences.get(i);
//				for(int j = 0; j < deviceSize; j++) {
//					DeviceLocation device = devices.get(j);
//					analysis.analysis(fence, device);
//					
//					GeometryFactory geometryFactory = new GeometryFactory();
//					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
//					DistanceOp.distance(fence.getPolygon(), point);
//				}
//			}
//	    	
//	    	long endTime = System.nanoTime();
//	    	double time = (endTime - startTime) / 1000000.00;
//	    	System.out.printf("Thread4 time spent: %.4f ms\n", time);
//	    }
//	}
//	public static class Thread10 implements Runnable {
//	    @Override
//	    public void run() {
//	    	long startTime = System.nanoTime();
//	    	PolygonAnalysis analysis = new PolygonAnalysis();
//	    	int fenceSize = fences.size();
//	    	int deviceSize = devices.size();
//	    	
//	    	for(int i = 9*fenceSize/8; i < fenceSize; i++) {
//	    		FenceGeometry fence  = fences.get(i);
//				for(int j = 0; j < deviceSize; j++) {
//					DeviceLocation device = devices.get(j);
//					analysis.analysis(fence, device);
//					
//					GeometryFactory geometryFactory = new GeometryFactory();
//					Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
//					DistanceOp.distance(fence.getPolygon(), point);
//				}
//			}
//	    	
//	    	long endTime = System.nanoTime();
//	    	double time = (endTime - startTime) / 1000000.00;
//	    	System.out.printf("Thread4 time spent: %.4f ms\n", time);
//	    }
//	}
	
	
	
	@Override
	public void run() {
//		fences  = new ArrayList<>();
//		devices = new ArrayList<>();
		PolygonAnalysis analysis = new PolygonAnalysis();
		
		//circleTest();
		//polygonTest();
		
		realPolygonTest();
		
		long startTime = System.nanoTime();
		System.out.print("Calculation start\n");
		
		// for each point created check if it's in the fences
		for(DeviceLocation device : devices) {
			GeometryFactory geometryFactory = new GeometryFactory();
			Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
			
			for(FenceGeometry fence : fences) {
				analysis.analysis(fence, device);
				//IsPointInPolygon(fence, point);
				DistanceOp.distance(fence.getPolygon(), point);
				//System.out.println(analysis.analysis(fence, device));
				//System.out.print("\n");
				//System.out.print(DistanceOp.distance(fence.getPolygon(), point));
			}
		}
		
		long endTime = System.nanoTime();
		System.out.println("Time spent: " + (endTime - startTime) / 1000000000 + " sec");
	}
	
	@Test
	public void Test() {
		fences  = new ArrayList<>();
		devices = new ArrayList<>();
		
		// Create fences, points and then clean the remainders in the fence list
		realTest();
		cleanRemainder();
		
		Thread1 t1 = new Thread1();
		Thread2 t2 = new Thread2();
		Thread3 t3 = new Thread3();
		Thread4 t4 = new Thread4();
		
		System.out.println("Analysis starts!");

		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
		new Thread(t4).start();
	}
	
	public static void main() {
//		BasicConfigurator.configure();
		fences  = new ArrayList<>();
		devices = new ArrayList<>();
		
		// Create fences, points and then clean the remainders in the fence list
		realTest();
		cleanRemainder();
//		System.out.print(Runtime.getRuntime().availableProcessors());

		// Using 1 thread:  316s
		// Using 2 threads: 162s
		// Using 4 threads: 104s 103s
		// Using 8 threads: 80s
		
		Thread1 t1 = new Thread1();
		Thread2 t2 = new Thread2();
		Thread3 t3 = new Thread3();
		Thread4 t4 = new Thread4();
		Thread5 t5 = new Thread5();
		Thread6 t6 = new Thread6();
		Thread7 t7 = new Thread7();
		Thread8 t8 = new Thread8();
//		Thread9 t9 = new Thread9();
//		Thread10 t10 = new Thread10();
		
		System.out.println("Analysis starts!");

		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
		new Thread(t4).start();
		new Thread(t5).start();
		new Thread(t6).start();
		new Thread(t7).start();
		new Thread(t8).start();
//		new Thread(t9).start();
//		new Thread(t10).start();
	}
	
	public static void cleanRemainder() {
		int size = fences.size();
		int threadNum = 8;		// Number of threads
		remainder = new ArrayList<>();
		
		if(size % threadNum != 0) {
			int remainSize = size % threadNum;
			//System.out.println(remainSize);
			
			for(int i = size-1; i >= size - remainSize; i--) {
				remainder.add(fences.remove(i));
			}
		}
//		for(Iterator<FenceGeometry> iterator = remainder.iterator(); iterator.hasNext();) {
//			System.out.println((iterator.next()).getPolygon());
//		}
	}
	
	/**
	 * Create polygon and circle fences, also create points as well
	 */
	public static void realTest() {
		// Creating polygon fence
		for(int i = 0; i < 4345; i++) {
			fences.add(createPolyFence());
		}
		
		for(int i = 0; i< 4154; i++) {
			fences.add(createCirFence());
		}
		
		// Creating 200000 device points
		for(int i = 0; i < 200000; i++) {
			devices.add(createPoints());
		}
	}
	
	
	/**
	 * Create 1 polygon fence with 3 to 15 vertices
	 * @return	FenceGeometry
	 */
	private static FenceGeometry createPolyFence() {
		String coord = "[";
		FenceBase base = new FenceBase();
		base.setFenceType(2);	// Polygon
//		int num = (int) (Math.random() * 12 + 3);	// num of vertices
//		System.out.println(num);
		int num = 17;
		
		for(int i = 0; i < num; i++) {
			GpsData gpsData = new GpsData();
			
			// Randomly creating latitude and longitude
			gpsData.setLat(Math.random()*5-33);	// -33 to -28
			gpsData.setLng(Math.random()*5-96);	// -96 to -91
		    
		    //System.out.println(deviceLocation);
		    
		    FenceUtil.updateFence(base, OptType.OPT_MOD);
		    //System.out.print("lat: "+gpsData.getLat());
		    String latPos = String.valueOf(gpsData.getLat()+0.0001*(-1+2*Math.random()));
		    String lngPos = String.valueOf(gpsData.getLng()+0.0001*(-1+2*Math.random()));
		    if(i == num - 1) {
		    	coord = coord.concat("{\"lat\":" + latPos + ",\"lng\":" + lngPos + "}");
		    }
		    else
		    	coord = coord.concat("{\"lat\":" + latPos + ",\"lng\":" + lngPos + "},");	
		}
		coord = coord.concat("]");
		//System.out.println(coord);
		
		PolygonAnalysis analysis = new PolygonAnalysis();
		
		return analysis.createGeometry(coord, base.getFenceType());
	}
	
	/**
	 * Create 1 circle fence with random radius
	 * @return FenceGeometry
	 */
	private static FenceGeometry createCirFence() {
		FenceBase base = new FenceBase();
		GpsData gpsData = new GpsData();
		int radius = (int) (Math.random()*5) + 1;
		base.setFenceType(1);
		
		// Randomly creating latitude and longitude
		gpsData.setLat(Math.random()*5-33);	// -33 to -28
		gpsData.setLng(Math.random()*5-96);	// -96 to -91
		
		FenceUtil.updateFence(base, OptType.OPT_MOD);
	    //System.out.print("lat: "+gpsData.getLat());
	    String latPos = String.valueOf(gpsData.getLat()+0.0001*(-1+2*Math.random()));
	    String lngPos = String.valueOf(gpsData.getLat()+0.0001*(-1+2*Math.random()));
	    
	    //String coordinate = "{\"lat\":25.634669392615848,\"lng\":104.105114,\"radius\":42}";
	    String coordinate = "{\"lat\":" + latPos + ",\"lng\":" + lngPos + ",\"radius\":" + radius +"}";
		CircleAnalysis analysis = new CircleAnalysis();
		
//		FenceGeometry fence = analysis.createGeometry(coordinate, base.getFenceType());
//		System.out.println(fence.getRadius());
		
		return  analysis.createGeometry(coordinate, base.getFenceType());
	}
	
	/**
	 * Create 1 device point
	 * @return DeviceLocation
	 */
	private static DeviceLocation createPoints() {
		DeviceLocation device = new DeviceLocation();
		
		device.setLat(Math.random()*5-33);	// -33 to -28
		device.setLng(Math.random()*5-96);	// -96 to -91
	    
	    return device;
	}
	
	 private boolean IsPointInPolygon(FenceGeometry fenceGeo, Point point)
		{
		    int i, j;
		    boolean c = false;
		    for (i = 0, j = fenceGeo.getPolygon().getCoordinates().length - 1; i < fenceGeo.getPolygon().getCoordinates().length; j = i++)
		    {
		        if ((((fenceGeo.getPolygon().getCoordinates()[i].y <= point.getY()) && (point.getY() < fenceGeo.getPolygon().getCoordinates()[j].y)) 
		                || ((fenceGeo.getPolygon().getCoordinates()[j].y <= point.getY()) && (point.getY() < fenceGeo.getPolygon().getCoordinates()[i].y))) 
		                && (point.getX() < (fenceGeo.getPolygon().getCoordinates()[j].x - fenceGeo.getPolygon().getCoordinates()[i].x) * (point.getY() - fenceGeo.getPolygon().getCoordinates()[i].y) 
		                    / (fenceGeo.getPolygon().getCoordinates()[j].y - fenceGeo.getPolygon().getCoordinates()[i].y) + fenceGeo.getPolygon().getCoordinates()[i].x))
		        {

		            c = !c;
		        }
		    }

		    return c;
		}
	
	private void polygonTest() {
		long startTime = System.nanoTime();
		fences  = new ArrayList<>();
		
		// Creating fences and device locations
		for(int i = 0; i < 1; i++) {
			FenceBase base = new FenceBase();
			base.setFenceType(2);
			
			GpsData gpsData = new GpsData();
			gpsData.setLat(Math.random()*5-33);
			gpsData.setLng(Math.random()*5-96);
			gpsData.setDeviceId((long) (Math.random()*900000000+100000000));
			
			DeviceLocation deviceLocation = new DeviceLocation();
//			deviceLocation.setDeviceId(gpsData.getDeviceId());
//			deviceLocation.setLat(gpsData.getLat());
//			deviceLocation.setLng(gpsData.getLng());
			
			// Device location 1
			GeometryFactory geometryFactory = new GeometryFactory();
			DeviceLocation device1 = new DeviceLocation();
			device1.setDeviceId((long) (Math.random()*900000000+100000000));
			device1.setLng(104.06496671149515);
			device1.setLat(30.660748202856904);
			Point point1 = geometryFactory.createPoint(new Coordinate(device1.getLng(), device1.getLat()));
			
			// Device location 2
			DeviceLocation device2 = new DeviceLocation();
			device2.setDeviceId((long) (Math.random()*900000000+100000000));
			device2.setLng(104.06292823264383);
			device2.setLat(30.66028675353522);
			Point point2 = geometryFactory.createPoint(new Coordinate(device2.getLng(), device2.getLat()));
			
			FenceUtil.addGpsData(deviceLocation);
			FenceUtil.start(fence);
			FenceUtil.updateFence(base, OptType.OPT_MOD);
			
			// Create a polygon fence
//			String coordinate = "[{\"lat\":30.662483232590247,\"lng\":104.06570700118326},{\"lat\":30.66045287554483,\"lng\":104.06450537154458},{\"lat\":30.65967763705711,\"lng\":104.06416204879068},{\"lat\":30.656355115966118,\"lng\":104.06429079482339},{\"lat\":30.656392033494562,\"lng\":104.06738069960855},{\"lat\":30.660120631240073,\"lng\":104.06733778426431}]";
//			PolygonAnalysis analysis = new PolygonAnalysis();
//			FenceGeometry fenceGeometry = analysis.createGeometry(coordinate, base.getFenceType());
//			System.out.println(JsonUtil.toJSON(deviceLocation));
//			System.out.println(analysis.analysis(fenceGeometry, deviceLocation));
			
			// Check the distance between the fence and point1
//			System.out.println(JsonUtil.toJSON(device1));
//			System.out.println(analysis.analysis(fenceGeometry, device1));
//			System.out.println(DistanceOp.distance(fenceGeometry.getPolygon(), point1));
//			System.out.println(fenceGeometry.getPolygon().getBoundary().distance(point1));
//			System.out.println(PositionUtil.degree2M(fenceGeometry.getPolygon().getBoundary().distance(point1)));
//			
//			// Check the distance between the fence and point2
//			System.out.println(JsonUtil.toJSON(device2));
//			System.out.println(analysis.analysis(fenceGeometry, device2));
//			System.out.println(DistanceOp.distance(fenceGeometry.getPolygon(), point2));
//			System.out.println(fenceGeometry.getPolygon().getBoundary().distance(point2));
//			System.out.println(PositionUtil.degree2M(fenceGeometry.getPolygon().getBoundary().distance(point2)));
			
			
			String coordinate2 = "[{\"lat\":30.0,\"lng\":104.0},{\"lat\":30.0,\"lng\":108.0},{\"lat\":33.0,\"lng\":104.0}]";
			PolygonAnalysis analysis = new PolygonAnalysis();
			FenceGeometry fenceGeometry = analysis.createGeometry(coordinate2, base.getFenceType());
			System.out.println(fenceGeometry.getPolygon().getCoordinates()[1]);
			DeviceLocation device3 = new DeviceLocation();
			device3.setDeviceId((long) (Math.random()*900000000+100000000));
			device3.setLng(106.0);
			device3.setLat(31.0);
			Point point3 = geometryFactory.createPoint(new Coordinate(device3.getLng(), device3.getLat()));
			
			System.out.println(JsonUtil.toJSON(device3));
			System.out.println(analysis.analysis(fenceGeometry, device3));
			System.out.println(DistanceOp.distance(fenceGeometry.getPolygon(), point3));
			System.out.println(fenceGeometry.getPolygon().getBoundary().distance(point3));
			System.out.println(PositionUtil.degree2M(fenceGeometry.getPolygon().getBoundary().distance(point3)));
			
//			double[] bd09 =GisUtil.convertCoor(new double[] { 30.659031600233988,104.06151604959749 }, GisUtil.CoordinateType.GCJ02, GisUtil.CoordinateType.WGS84);
//			System.out.println(bd09[0] + "," + bd09[1]);
		}
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration/1000000);
	}
	
	private void circleTest() {		
		long startTime = System.nanoTime();
		
		// Creating 10000 fences and device locations
		for(int i = 0; i < 10000; i++) {		
			FenceBase base = new FenceBase();
			base.setFenceType(1);
			//Date date = new Date();
			
			GpsData gpsData = new GpsData();
			gpsData.setLat(Math.random()*361-180);
			gpsData.setLng(Math.random()*181-90);
			gpsData.setDeviceId((long) (Math.random()*900000000+100000000));
			//gpsData.setDt();
			
			DeviceLocation deviceLocation = new DeviceLocation();
		    deviceLocation.setDeviceId(gpsData.getDeviceId());
		    //deviceLocation.setDt(gpsData.getDt());
		    deviceLocation.setLat(gpsData.getLat());
		    deviceLocation.setLng(gpsData.getLng());
		    //logger.info(debug, "GatewayHandlerThread--5 deviceLocation:" + JsonUtil.toJSON(deviceLocation));
		    
		    FenceUtil.addGpsData(deviceLocation);
		    //System.out.println(deviceLocation);
		    FenceUtil.start(fence);
		    //System.out.println(OptType.OPT_MOD);
		    FenceUtil.updateFence(base, OptType.OPT_MOD );
		    //System.out.print("lat: "+gpsData.getLat());
		    String latPos = String.valueOf(gpsData.getLat()+0.0001*(-1+2*Math.random()));
		    String lngPos = String.valueOf(gpsData.getLng()+0.0001*(-1+2*Math.random()));
		    
		    //String coordinate = "{\"lat\":25.634669392615848,\"lng\":104.105114,\"radius\":42}";
		    String coordinate = "{\"lat\":" + latPos + ",\"lng\":" + lngPos + ",\"radius\":7}";
			CircleAnalysis analysis = new CircleAnalysis();
			FenceGeometry fenceGeometry = analysis.createGeometry(coordinate, base.getFenceType());
		    //fence.setMinute();
		    //fenceCallBack.start(fence);
		    //fence.updateFence();
		    //System.out.println(fence);
		    System.out.println(JsonUtil.toJSON(deviceLocation));
			System.out.println(analysis.analysis(fenceGeometry, deviceLocation));
		    //analysis.analysis(fenceGeometry, deviceLocation);
//			if(analysis.analysis(fenceGeometry, deviceLocation)) {
//				System.out.println("added");
//			}
			//System.out.println(gpsData.getLat() + " " + gpsData.getLng());
		}
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration/1000000);
	}
	
	// Radius = distance between two points
	private boolean inCircle(String coordinate, DeviceLocation deviceLocation) {
		// Center location
		JsonNode jsonNode = JsonUtil.toJSONObject(coordinate);
		double Cx = jsonNode.get("lng").asDouble();
		double Cy = jsonNode.get("lat").asDouble();
		double radius = jsonNode.get("radius").asDouble();
		
		
		// Device location
		double Dx = deviceLocation.getLng();
		double Dy = deviceLocation.getLat();
		
		double distance = PositionUtil.getDistance(Cx, Cy, Dx, Dy);
		
		return radius * 1000 > distance;
	}
	
	private static void realPolygonTest() {
		//fences  = new ArrayList<>();
		//devices = new ArrayList<>();
		//PolygonAnalysis analysis = new PolygonAnalysis();

		// Creating 10000 fences
		for(int i = 0; i < 100; i++) {
			fences.add(createPolyFence());
		}

		// Creating 200000 device points
		for(int i = 0; i < 2000; i++) {
			devices.add(createPoints());
		}

		//long startTime = System.nanoTime();
		//
		//// for each point created check if it's in the fences
		//for(DeviceLocation device : devices) {
//			GeometryFactory geometryFactory = new GeometryFactory();
//			Point point = geometryFactory.createPoint(new Coordinate(device.getLng(), device.getLat()));
		//	
//			for(FenceGeometry fence : fences) {
//				analysis.analysis(fence, device);
//				//IsPointInPolygon(fence, point);
//				DistanceOp.distance(fence.getPolygon(), point);
//				//System.out.println(analysis.analysis(fence, device));
//				//System.out.print("\n");
//				//System.out.print(DistanceOp.distance(fence.getPolygon(), point));
//			}
		//}
		//
		//long endTime = System.nanoTime();
		//System.out.println("Time spent: " + (endTime - startTime) / 1000000000 + " sec");
	}
}
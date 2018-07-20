package com.lfyt.mobile.android.log;



import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public abstract class Logger {

	protected Logger(){}

	///////////////////////////////////////////////////////////////////////////
	// Logger Configuration
	///////////////////////////////////////////////////////////////////////////

	//TAGS
	private static String APP_TAG;

	public static int MAX_SIZE_OBJECT_NAME = 25;
	public static int MAX_TAG_SIZE = 15;
	public static boolean LOG_TIME = false;

	private static LoggerInterface loggerInterface;

	public interface LoggerInterface {
		String tag(Object object);
	}



	///////////////////////////////////////////////////////////////////////////
	// Logger Setup
	///////////////////////////////////////////////////////////////////////////

	public final static void setup(String baseAppTag, LoggerInterface instance)
	{
		APP_TAG = " #"+baseAppTag+" | ";

		loggerInterface = instance;

		Timber.plant( new Timber.DebugTree() );

		Logger.SPACE();
		Logger.I(Logger.class, "LOGGER INITIALIZED");
		Logger.SPACE();
	}





	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	// Log Interface
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////


	public static void SPACE() {Timber.d(APP_TAG); }




	//Debug
	public static void D(Object object, String message, Object... args){
		LOG(Level.DEBUG, objectTagify(object), object, message, args);
	}

	//Info
	public static void I(Object object, String message, Object... args){
		LOG(Level.INFO, objectTagify(object), object, message, args);
	}

	//Error
	public static void E(Object object, String message, Object... args){
		LOG(Level.ERROR, objectTagify(object), object, message, args);
	}


	//Exception
	public static void EXP(Object caller, String message, Exception e) {
		exception(caller, message, e);
	}
	public static void EXP(Object caller, Exception e) {
		exception(caller, "EMPTY", e);
	}



	//Dependency Injection
	public static void DI(Object caller){
		debug(caller, tagify("DEPENDENCY"), "~> CREATED <~");
	}





	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	// Logging Method
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public enum Level{
		DEBUG, INFO, ERROR
	}

	private static void LOG(Level level, String TAG, Object caller, String message, Object... args){
		switch (level){
			case DEBUG:
				debug(caller, TAG, message, args);
				break;
			case INFO:
				info(caller, TAG, message, args);
				break;
			case ERROR:
				error(caller, TAG, message, args);
				break;
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// Info
	///////////////////////////////////////////////////////////////////////////

	private static void info(Object caller, String logTag, String message, Object... args){
		try{
			message = new StringBuilder()
					.append(APP_TAG)
					.append(time())
					.append(logTag)
					.append(caller==null?"":name(caller))
					.append(" -> ")
					.append(message)
					.toString();

			Timber.i(message, args);
		}catch (Exception e){
			try{
				Timber.e( "| LOG ERROR | LEVEL -> INFO | EXCEPTION -> %s | MESSAGE -> %s | PARAMS -> %s", e.getClass().getSimpleName(), message, args.toString() );
			}catch (Exception e2){
				Timber.e("LOGGING ERROR NO RECOVERY [INFO]");
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// Debug
	///////////////////////////////////////////////////////////////////////////
	private static void debug(Object caller, String logTag, String message, Object... args){
		try{

			message = new StringBuilder()
					.append(APP_TAG)
					.append(time())
					.append(logTag)
					.append(caller==null?"":name(caller))
					.append(" -> ")
					.append(message)
					.toString();

			Timber.d(message, args);
		}catch (Exception e){

			try{
				Timber.e( "| LOG ERROR | LEVEL -> DEBUG | EXCEPTION -> %s | MESSAGE -> %s | PARAMS -> %s", e.getClass().getSimpleName(), message, args.toString() );
			}catch (Exception e2){
				Timber.e("LOGGING ERROR NO RECOVERY [DEBUG]");
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// Error
	///////////////////////////////////////////////////////////////////////////
	private static void error(Object caller, String logTag, String message, Object... args){
		try{

			message = new StringBuilder()
					.append(APP_TAG)
					.append(time())
					.append(logTag)
					.append(caller==null?"":name(caller))
					.append(" -> ")
					.append(message)
					.toString();

			Timber.e(message, args);
		}catch (Exception e){
			try{
				Timber.e( "| LOG ERROR | LEVEL -> ERROR | EXCEPTION -> %s | MESSAGE -> %s | PARAMS -> %s", e.getClass().getSimpleName(), message, args.toString() );
			}catch (Exception e2){
				Timber.e("LOGGING ERROR NO RECOVERY [ERROR]");
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// Exception
	///////////////////////////////////////////////////////////////////////////
	private static void exception(Object caller, String message, Exception e) {
		try{
			StackTraceElement[] list = new Exception().getStackTrace();
			StackTraceElement l = list[2]; // 0 == EXCEPTION || 1 == EXP (INTERFACE) || 2 = METHOD

			String aux = e.getClass().getSimpleName()
					+ " --> "
					+ l.getClassName()+"."+l.getMethodName()+":"+l.getLineNumber();

			error(caller, objectTagify(caller), aux );

			e.printStackTrace();

		}
		catch (Exception error){
			Timber.e("LOG EXCEPTION ERROR | %s ==> %s", name(caller), message);
		}
	}






	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	// Utility Methods
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////////////////
	// Time
	///////////////////////////////////////////////////////////////////////////
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

	private static String time() {
		if( LOG_TIME ) return dateFormat.format(new Date()) + " | ";
		else return "";
	}


	///////////////////////////////////////////////////////////////////////////
	// Get Log Caller Name
	///////////////////////////////////////////////////////////////////////////

	private static String name(Object caller) {
		if( caller.getClass().getSimpleName().length() > MAX_SIZE_OBJECT_NAME){
			return "#"+caller.getClass().getSimpleName().substring(0, MAX_SIZE_OBJECT_NAME);
		}
		return "#"+caller.getClass().getSimpleName();
	}






	///////////////////////////////////////////////////////////////////////////
	// Smart Tagging
	///////////////////////////////////////////////////////////////////////////

	private static String objectTagify(Object object){
		return tagify(loggerInterface.tag(object));
	}

	private static String tagify(String tag) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(tag);

		for(int i=0; i<MAX_TAG_SIZE; i++) stringBuilder.append(" ");

		String tagify = "#" + stringBuilder.toString().substring(0, MAX_TAG_SIZE) + " | ";

		return tagify;
	}

}

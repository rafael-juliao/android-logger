package com.lfyt.mobile.android.log;



import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public abstract class Logger {


	public Logger(){
	}



	///////////////////////////////////////////////////////////////////////////
	// Logger Configuration
	///////////////////////////////////////////////////////////////////////////


	public interface TagObjectInterface{
		String tag(Object object);
	}


	public static int MAX_TAG_SIZE = 15;
	public static boolean LOG_TIME = false;

	private static TagObjectInterface tagObjectInterface;

	public final static void setup(String baseAppTag, TagObjectInterface tagObjectInterface)
	{
		APP_TAG = " ~"+baseAppTag+"~ | ";

		Logger.tagObjectInterface = tagObjectInterface;

		Timber.plant( new Timber.DebugTree() );

		Logger.SPACE();
		Logger.I(Logger.class, "LOG CLASS INITIALIZED");
		Logger.SPACE();
	}


	//TAGS
	private static String APP_TAG;

	private final static String ERROR_TAG = " ~~> ERROR <~~ | ";

	private final static String DEPENDENCY  = "[DEPENDENCY]  |  ";




	///////////////////////////////////////////////////////////////////////////
	// Log Interface
	///////////////////////////////////////////////////////////////////////////

	public static void SPACE() {
		empty();
	}




	//Debug
	public static void D(Object object, String message, Object... args){
		LOG(Level.DEBUG, tagging(object), object, message, args);
	}

	private static String tagging(Object object) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(tagObjectInterface.tag(object));

		for(int i=0; i<MAX_TAG_SIZE; i++) stringBuilder.append(" ");

		String tag = stringBuilder.toString().substring(0, MAX_TAG_SIZE) + " |";

		return tag;
	}

	//Info
	public static void I(Object object, String message, Object... args){
		LOG(Level.INFO, tagging(object), object, message, args);
	}

	//Error
	public static void E(Object object, String message, Object... args){
		LOG(Level.ERROR, tagging(object), object, message, args);
	}




	//Dependency Injection
	public static void DI(Object caller){
		debug(caller, DEPENDENCY, "~> CREATED <~");
	}




	//Log Exception
	public static void EXP(Object caller, String message, Exception e) {
		exception(caller, message, e);
	}

	public static void EXP(Object caller, Exception e) {
		exception(caller, "EMPTY", e);
	}


	///////////////////////////////////////////////////////////////////////////
	// Log Functions
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
	// Methods That Executed The Business
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

	private static void error(Object caller, String logTag, String message, Object... args){
		try{

			message = new StringBuilder()
					.append(APP_TAG)
					.append(time())
					.append(logTag)
					.append(ERROR_TAG)
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

	private static void exception(Object caller, String message, Exception e) {
		try{
			StackTraceElement l = new Exception().getStackTrace()[0];

			String aux = e.getClass().getSimpleName()
					+ " --> "
					+ l.getClassName()+"."+l.getMethodName()+":"+l.getLineNumber();

			error(caller, " [EXCEPTION] ", aux );

			e.printStackTrace();

		}
		catch (Exception error){
			Timber.e("LOG EXCEPTION ERROR | %s ==> %s", name(caller), message);
		}
	}




	///////////////////////////////////////////////////////////////////////////
	// Utility Methods Methods
	///////////////////////////////////////////////////////////////////////////


	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

	private static String time() {
		if( LOG_TIME ) return dateFormat.format(new Date()) + " | ";
		else return "";
	}

	private static String name(Object caller) {
		int MAX_SIZE = 25;
		if( caller.getClass().getSimpleName().length() > MAX_SIZE){
			return caller.getClass().getSimpleName().substring(0, MAX_SIZE);
		}
		return caller.getClass().getSimpleName();
	}

	private static void empty(){
		Timber.d(APP_TAG);
	}


}

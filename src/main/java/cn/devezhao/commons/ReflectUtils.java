package cn.devezhao.commons;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public final class ReflectUtils {
	
	private static final Class<?>[] OBJECT = new Class[] { Object.class };
	private static final Class<?>[] NO_PARAM = new Class[] { };

	private static final Method OBJECT_EQUALS;
	private static final Method OBJECT_HASHCODE;
	static {
		try {
			OBJECT_EQUALS = Object.class.getMethod("equals", OBJECT);
			OBJECT_HASHCODE = Object.class.getMethod("hashCode", NO_PARAM);
		} catch (Exception e) {
			throw new RuntimeException("无法找到 Object#equals 或 Object#hashCode 方法", e);
		}
	}

	/**
	 * 复写 {@link Object#equals(Object)}
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean overridesEquals(Class<?> clazz) {
		Method equals;
		try {
			equals = clazz.getMethod("equals", OBJECT);
		} catch (NoSuchMethodException nsme) {
			return false; // its an interface so we can't really tell anything...
		}
		return !OBJECT_EQUALS.equals(equals);
	}

	/**
	 * 复写 {@link Object#hashCode()}
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean overridesHashCode(Class<?> clazz) {
		Method hashCode;
		try {
			hashCode = clazz.getMethod("hashCode", NO_PARAM);
		} catch (NoSuchMethodException nsme) {
			return false; // its an interface so we can't really tell anything...
		}
		return !OBJECT_HASHCODE.equals(hashCode);
	}

	/**
	 * 使用当前线程加载 Class
	 * 
	 * @param clazzName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String clazzName)
			throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(clazzName);
			}
		} catch (Throwable t) {
		}
		return Class.forName(clazzName);
	}

	/**
	 * 使用调用者线程加载 Class
	 * 
	 * @param clazzName
	 * @param caller
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String clazzName, Class<?> caller)
			throws ClassNotFoundException {
		try {
			return Class.forName(clazzName, true, caller.getClassLoader());
		} catch (Throwable e) {
		}
		return classForName(clazzName);
	}
	
	/**
	 * 类是否 <code>public</code>
	 * 
	 * @param clazz
	 * @param member
	 * @return
	 */
	public static boolean isPublicClass(Class<?> clazz, Member member) {
		return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 类是否 <code>abstract</code>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isAbstractClass(Class<?> clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
	}

	/**
	 * 类是否 <code>final</code>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isFinalClass(Class<?> clazz) {
		return Modifier.isFinal(clazz.getModifiers());
	}

	/**
	 * 字段是否 "public static final"
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
	
	/**
	 * 获取修饰符的字符串表示
	 * 
	 * @param mod
	 * @return
	 */
	public static String getModifierString(int mod) {
		StringBuilder sb = new StringBuilder(32);
		if (Modifier.isPublic(mod)) {
			sb.append("public ");
		}
		if (Modifier.isProtected(mod)) {
			sb.append("protected ");
		}
		if (Modifier.isPrivate(mod)) {
			sb.append("private ");
		}
		if (Modifier.isAbstract(mod)) {
			sb.append("abstract ");
		}
		if (Modifier.isStatic(mod)) {
			sb.append("static ");
		}
		if (Modifier.isFinal(mod)) {
			sb.append("final ");
		}
		if (Modifier.isSynchronized(mod)) {
			sb.append("synchronized ");
		}
		if (Modifier.isTransient(mod)) {
			sb.append("transient ");
		}
		if (Modifier.isVolatile(mod)) {
			sb.append("volatile ");
		}
		if (Modifier.isStrict(mod)) {
			sb.append("strictfp ");
		}
		if (Modifier.isNative(mod)) {
			sb.append("native ");
		}
		if (Modifier.isInterface(mod)) {
			sb.append("interface ");
		}
		return sb.toString().trim();
	}
	
	/**
	 * 获取父级类
	 * 
	 * @param clazz
	 * @return
	 */
	public static Class<?> getSuperclassGenricType(Class<?> clazz) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		
		Type[] genTypes = ((ParameterizedType) genType).getActualTypeArguments();
		if (!(genTypes[0] instanceof Class<?>)) {
			return Object.class;
		}
		
		return (Class<?>) genTypes[0];
	}
	
	/**
	 * 获取所有子类
	 * 
	 * @param scanPackage
	 * @param superClass
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> getAllSubclasses(String scanPackage, Class<?> superClass)
			throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new LinkedHashSet<>();

		boolean recursive = true;
		String packageName = scanPackage;
		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
		while (dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				String filePath = URLDecoder.decode(url.getFile(), "utf-8");
				scanClassesInPackage(packageName, filePath, recursive, superClass, classes);
			} else if ("jar".equals(protocol)) {
				JarFile jar;
				jar = ((JarURLConnection) url.openConnection()).getJarFile();

				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.charAt(0) == '/') {
						name = name.substring(1);
					}

					if (name.startsWith(packageDirName)) {
						int idx = name.lastIndexOf('/');
						if (idx != -1) {
							packageName = name.substring(0, idx).replace('/', '.');
						}

						if ((idx != -1) || recursive) {
							if (name.endsWith(".class") && !entry.isDirectory()) {
								String className = name.substring(packageName.length() + 1, name.length() - 6);
								addScanClass(packageName + '.' + className, superClass, classes);
							}
						}
					}
				}
			}
		}

		return classes;
	}

	/**
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param superClass
	 * @param classes
	 * @throws ClassNotFoundException
	 */
	private static void scanClassesInPackage(String packageName, String packagePath, boolean recursive, Class<?> superClass,
			Set<Class<?>> classes) throws ClassNotFoundException {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}

		File[] dirfiles = dir.listFiles(new FileFilter() {
			@Override
            public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {
			if (file.isDirectory()) {
				scanClassesInPackage(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, superClass, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				addScanClass(packageName + '.' + className, superClass, classes);
			}
		}
	}
	
	/**
	 * @param className
	 * @param superClass
	 * @param into
	 * @throws ClassNotFoundException
	 */
	private static void addScanClass(String className, Class<?> superClass, Set<Class<?>> into) throws ClassNotFoundException {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		if (superClass == null || (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz))) {
			into.add(clazz);
		}
	}
	
	private ReflectUtils() {}
}

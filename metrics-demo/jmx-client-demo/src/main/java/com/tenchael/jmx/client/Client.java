package com.tenchael.jmx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Client {

	private static final String DOMAIN_PREFIX = "metrics.";
	private static final String ONAME_ATTR_NAME = "Oname";

	private static final Pattern ONAME_PATTERN = Pattern
			.compile(DOMAIN_PREFIX + "(\\w+):type=(\\w+),name=[\"]?([-.#/\\w]+)[\"]?");

	public static void main(String[] args) throws Exception {
		// Create an RMI connector client and
		// connect it to the RMI connector server
		echo("Start to connect jmx service...\n");
		JMXServiceURL url =
				new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);


		// Get an MBeanServerConnection
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		// Query MBean names
		Set<ObjectName> names =
				new TreeSet<>(mbsc.queryNames(null, null));
		Dict dict = new Dict();
		for (ObjectName name : names) {
			if (!name.toString().startsWith(DOMAIN_PREFIX)) {
				continue;
			}

			MBeanInfo mBeanInfo = mbsc.getMBeanInfo(name);
			List<String> attrNames = Arrays.stream(mBeanInfo.getAttributes())
					.map(inf -> inf.getName())
					.collect(Collectors.toList());

			for (String attrName : attrNames) {
				if (ONAME_ATTR_NAME.equals(attrName)) {
					continue;
				}
				Object attrValue = mbsc.getAttribute(name, attrName);
//				echo(attrName + ": " + attrValue);
				String kName = getName(name);
				String type = getType(name);
				dict.put(kName, type, attrName, attrValue);
			}


		}

		List list = dict.getList();
		echo(JSON.toJSONString(list, SerializerFeature.PrettyFormat));

		// Close MBeanServer connection
		//
		jmxc.close();
		echo("\nBye! Bye!");
	}

	private static String getName(ObjectName oname) {
		return getMatchedContent(ONAME_PATTERN, oname.toString(), 3);
	}

	private static String getType(ObjectName oname) {
		String domain = getMatchedContent(ONAME_PATTERN, oname.toString(), 1);
		String type = getMatchedContent(ONAME_PATTERN, oname.toString(), 2);
		return type + "." + domain;
	}

	private static void echo(String msg) {
		System.out.println(msg);
	}


	/**
	 * 得到符合模式的内容
	 *
	 * @param pattern
	 * @param input
	 * @param index
	 * @return
	 */
	public static String getMatchedContent(Pattern pattern, String input, int index) {
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			return matcher.group(index);
		}
		return null;
	}


}

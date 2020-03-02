package com.tenchael.jmx.ext;

import javax.management.ObjectName;

/**
 * MBean with object name
 * Created by Tenchael on 2019/11/26.
 */
public interface MBean {

	ObjectName getOname();

	abstract class BaseMBean implements MBean {
		private final ObjectName oname;

		public BaseMBean(ObjectName oname) {
			this.oname = oname;
		}

		@Override
		public ObjectName getOname() {
			return oname;
		}
	}
}

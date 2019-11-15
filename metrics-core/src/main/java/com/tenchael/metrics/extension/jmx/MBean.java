package com.tenchael.metrics.extension.jmx;

public interface MBean {

    abstract class BaseMBean implements MBean {

        private String category;
        private String name;

        public BaseMBean(String category, String name) {
            this.category = category;
            this.name = name;
        }


        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getName() {
            return name;
        }
    }


    String getCategory();

    String getName();


}
package br.com.mtinet.core.dataBase;



public class ColumnMetaData implements Comparable {
    public String name = null;
    public String classname = null;
    public String typename = null;
    public int index = 0;
    public int datatype = 0;
    public int precision = 0;
    public int scale = 0;
    public int size = 0;

    public String toString() {
      return "(" + index + ", "
        + name + ", "
        + classname + ", "
        + typename + ", "
        + datatype + ", "
        + precision + ", "
        + scale + ")";
    }

    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof ColumnMetaData))
        return false;
      ColumnMetaData other = (ColumnMetaData) obj;
      return (name != null && name.equalsIgnoreCase(other.name));
    }

    public int compareTo(Object obj) {
      ColumnMetaData c = (ColumnMetaData) obj;
      if (index < c.index)
        return -1;
      else if (index > c.index)
        return 1;
      else return 0;
    }

  }


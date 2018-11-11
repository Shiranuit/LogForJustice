package fr.Shiranuit.LogForJustice.Area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.base.Objects;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Events.EventsOther;
import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.Permission;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Flag {
	
	public static Flag NULL = new Flag(FlagType.NULL);
	
	private FlagType type;
	private Object value;
	private String key;
	private Configuration config;
	private Property prop;
	private FlagPerm perm;
	private String description = "";
	
	public Flag(Configuration config,Flag flag) {
		this.type = flag.type;
		this.value = flag.getDefaultValue();
		this.description = flag.getDescription();
		this.key = flag.getName();
		this.perm = flag.getPermLevel();
		this.config = config;
		this.prop = init(this.config, this.key, this.value, this.description);
	}
	
	public Flag(FlagType type) {
		this.type = type;
		this.value = def(type);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(FlagType type, String desc) {
		this.type = type;
		this.value = def(type);
		this.description = desc;
		this.perm = FlagPerm.OP;
	}
	
	public Flag(FlagType type, FlagPerm perm) {
		this.type = type;
		this.value = def(type);
		this.perm = perm;
	}
	
	public Flag(FlagType type, FlagPerm perm, String desc) {
		this.type = type;
		this.value = def(type);
		this.perm = perm;
		this.description = desc;
	}
	
	public Flag(FlagType type, Object defaultValue) {
		this.type = type;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(FlagType type, Object defaultValue, String desc) {
		this.type = type;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.perm = FlagPerm.OP;
		this.description = desc;
	}
	
	public Flag(FlagType type, FlagPerm perm, Object defaultValue) {
		this.type = type;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.perm = perm;
	}
	
	public Flag(FlagType type, FlagPerm perm, Object defaultValue, String desc) {
		this.type = type;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.perm = perm;
		this.description = desc;
	}
	
	public Flag(Configuration config, FlagType type, String key) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.value = def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(Configuration config, FlagType type, String key, String desc) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.value = def(type);
		this.description = desc;
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(Configuration config, FlagType type, String key, FlagPerm perm) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.value = def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = perm;
	}
	
	public Flag(Configuration config, FlagType type, String key, FlagPerm perm, String desc) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.description = desc;
		this.value = def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = perm;
	}
	
	public Flag(Configuration config, FlagType type, String key, Object defaultValue) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(Configuration config, FlagType type, String key, Object defaultValue, String desc) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.description = desc;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = FlagPerm.OP;
	}
	
	public Flag(Configuration config, FlagType type, String key, Object defaultValue, FlagPerm perm) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = perm;
	}
	
	public Flag(Configuration config, FlagType type, String key, Object defaultValue, FlagPerm perm, String desc) {
		this.config = config;
		this.type = type;
		this.key = key;
		this.description = desc;
		this.value = checkType(type, defaultValue) ? defaultValue : def(type);
		this.prop = init(this.config, this.key, this.value, this.description);
		this.perm = perm;
	}
	
	private Property init(Configuration config, String key, Object value, String description) {
		if (this.type == FlagType.BOOLEAN) {
			return config.get("flags",key,(Boolean)value, description);
		} else if (this.type == FlagType.INT) {
			return config.get("flags",key,(Integer)value, description);
		} else if (this.type == FlagType.LONG) {
			return config.get("flags",key,(Long)value, description);
		} else if (this.type == FlagType.DOUBLE) {
			return config.get("flags",key,(Double)value, description);
		} else if (this.type == FlagType.STRING) {
			return config.get("flags",key,(String)value, description);
		} else if (this.type == FlagType.LIST_STRING) {
			return config.get("flags",key,(String[])value, description);
		} else if (this.type == FlagType.PERMISSION) {
			return config.get("flags",key,((Permission)value).toArray(), description);
		} else if (type == FlagType.NULL) {
			return null;
		}
		throw new RuntimeException("Unknown FlagType");
	}
	
	private Object def(FlagType type) {
		if (type == FlagType.BOOLEAN) {
			return false;
		} else if (type == FlagType.INT) {
			return 0;
		} else if (type == FlagType.LONG) {
			return 0L;
		} else if (type == FlagType.DOUBLE) {
			return 0D;
		} else if (type == FlagType.STRING) {
			return "";
		} else if (type == FlagType.LIST_STRING) {
			return new String[] {};
		} else if (type == FlagType.PERMISSION) {
			return new Permission();
		} else if (type == FlagType.NULL) {
			return null;
		}
		throw new RuntimeException("Unknown FlagType");
	}
	
	private boolean checkType(FlagType type, Object obj) {
		if (obj != null) {
			if (type == FlagType.BOOLEAN && obj instanceof Boolean) {
				return true;
			} else if (type == FlagType.INT && obj instanceof Integer) {
				return true;
			} else if (type == FlagType.LONG && obj instanceof Long) {
				return true;
			} else if (type == FlagType.DOUBLE && obj instanceof Double) {
				return true;
			} else if (type == FlagType.STRING && obj instanceof String) {
				return true;
			} else if (type == FlagType.LIST_STRING && obj instanceof String[]) {
				return true;
			} else if (type == FlagType.PERMISSION && obj instanceof Permission) {
				return true;
			}
		}
		return false;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean isNull() {
		return this.type == FlagType.NULL;
	}
	
	
	public FlagType getType() {
		return this.type;
	}
	
	public String getName() {
		if (!isNull()) {
			return this.key;
		}
		return null;
	}
	
	public Object getDefaultValue() {
		if (!isNull()) {
			return this.value;
		}
		return null;
	}
	
	public FlagPerm getPermLevel() {
		if (!isNull()) {
			return this.perm;
		}
		return FlagPerm.NULL;
	}
	
	public Object getValue() {
		if (this.prop != null && !isNull()) {
			if (this.type == FlagType.BOOLEAN) {
				return this.prop.getBoolean();
			} else if (this.type == FlagType.INT) {
				return this.prop.getInt();
			} else if (this.type == FlagType.LONG) {
				return this.prop.getLong();
			} else if (this.type == FlagType.DOUBLE) {
				return this.prop.getDouble();
			} else if (this.type == FlagType.STRING) {
				return this.prop.getString();
			} else if (this.type == FlagType.LIST_STRING) {
				return this.prop.getStringList();
			} else if (this.type == FlagType.PERMISSION) {
				return new Permission(((Permission)this.value).defaultValue()).process(ArrayConverter.convert(this.prop.getStringList()));
			}
		}
		return null;
	}
	
	public int length() {
		if (!isNull() && this.type == FlagType.STRING) {
			return ((String)getValue()).length();
		}
		return -1;
	}
	
	public Object convert(String value) {
		try {
			if (this.prop != null) {
				if (this.type == FlagType.BOOLEAN) {
					return Boolean.valueOf(value);
				} else if (this.type == FlagType.INT) {
					return Integer.valueOf(value);
				} else if (this.type == FlagType.LONG) {
					return Long.valueOf(value);
				} else if (this.type == FlagType.DOUBLE) {
					return Double.valueOf(value);
				} else if (this.type == FlagType.STRING) {
					return value;
				} else if (this.type == FlagType.LIST_STRING) {
					if (value.startsWith("[") && value.endsWith("]")) {
						 value = value.substring(1, value.length()-1);
						 String[] elements = value.split(",");
						 return elements;
					}
					return null;
				} else if (this.type == FlagType.PERMISSION) {
					if (value.startsWith("[") && value.endsWith("]")) {
						 value = value.substring(1, value.length()-1);
						 String[] elements = value.split(",");
						 return new Permission(((Permission)this.value).defaultValue(),elements);
					}
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean setValue(Object obj) {
		if (this.prop != null && !isNull()) {
			if (checkType(this.type, obj)) {
				if (this.type == FlagType.BOOLEAN) {
					this.config.getCategory("flags").get(this.key).set((Boolean)obj);
				} else if (this.type == FlagType.INT) {
					this.config.getCategory("flags").get(this.key).set((Integer)obj);
				} else if (this.type == FlagType.LONG) {
					this.config.getCategory("flags").get(this.key).set((Long)obj);
				} else if (this.type == FlagType.DOUBLE) {
					this.config.getCategory("flags").get(this.key).set((Double)obj);
				} else if (this.type == FlagType.STRING) {
					String txt = (String)obj;
					if (txt.equals("\"\"")) {
						txt = "";
					}
					this.config.getCategory("flags").get(this.key).set(txt);
				} else if (this.type == FlagType.LIST_STRING) {
					this.config.getCategory("flags").get(this.key).set((String[])obj);
				} else if (this.type == FlagType.PERMISSION) {
					Permission perm = (Permission)obj;
					this.config.getCategory("flags").get(this.key).set(perm.toArray());
				}
				this.config.save();
				return true;
			}
		}
		return false;
	}
	
	
	
	private FlagType classToFlag(Class obj) {
		if (obj.isInstance(Boolean.class)) {
			return FlagType.BOOLEAN;
		} else if (obj.isInstance(String.class)) {
			return FlagType.STRING;
		} else if (obj.isInstance(Integer.class)) {
			return FlagType.INT;
		} else if (obj.isInstance(Long.class)) {
			return FlagType.LONG;
		} else if (obj.isInstance(Double.class)) {
			return FlagType.DOUBLE;
		} else if (obj.isInstance(String[].class)) {
			return FlagType.LIST_STRING;
		} else if (obj.isInstance(Permission.class)) {
			return FlagType.PERMISSION;
		}
		return FlagType.NULL;
	}
	
	public boolean check(Class clazz) {
		return classToFlag(clazz) == this.type;
	}
	
	public boolean check(FlagType flagType) {
		return flagType == this.type;
	}
	
	public boolean contains(Object obj) {
		if (!isNull()) {
			 if (this.type == FlagType.LIST_STRING && obj instanceof String) {
				ArrayList lst = new ArrayList<>(Arrays.asList((String[])getValue()));
				return lst.contains(((String)obj).toString());
			}
		}
		return false;
	}
	
	public int size() {
		if (!isNull()) {
			if (this.type == FlagType.LIST_STRING) {
				ArrayList<String> lst = new ArrayList<String>(Arrays.asList((String[])getValue()));
				return lst.size();
			}
		}
		return -1;
	}
	
	public boolean add(Object obj) {
		if (!isNull()) {
			if (this.type == FlagType.LIST_STRING && obj instanceof String) {
				ArrayList<String> lst = new ArrayList<String>(Arrays.asList((String[])getValue()));
				boolean success = lst.add(((String)obj).toString());
				String[] stockArr = new String[lst.size()];
				lst.toArray(stockArr);
				setValue(stockArr);
				return success;
			}
		}
		return false;
	}
	
	public Object remove(Object obj) {
		if (!isNull()) {
			if (this.type == FlagType.LIST_STRING && obj instanceof String) {
				ArrayList lst = new ArrayList<>(Arrays.asList((String[])getValue()));
				Object success = lst.remove(((String)obj).toString());
				String[] stockArr = new String[lst.size()];
				lst.toArray(stockArr);
				setValue(stockArr);
				return success;
			}
		}
		return null;
	}
	
	public boolean existPermission(String name) {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).existPermission(name);
			}
		}
		return false;
	}
	
	public int permissionPower(String name) {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).permissionPower(name);
			}
		}
		return 0;
	}
	
	public boolean hasPermission(String name, int permLevel) {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).hasPermission(name, permLevel);
			}
		}
		return false;
	}
	
	public Permission process(ArrayList<String> lst) {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).process(lst);
			}
		}
		return null;
	}
	
	public String[] toArray() {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).toArray();
			}
		}
		return new String[] {};
	}
	
	public void apply(HashMap<String, Integer> map) {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				((Permission)getValue()).apply(map);
			}
		}
	}
	
	public void clear() {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				((Permission)getValue()).clear();
			}
		}
	}
	
	public HashMap<String, Integer> permission() {
		if (!isNull()) {
			if (this.type == FlagType.PERMISSION) {
				return ((Permission)getValue()).permission;
			}
		}
		return null;
	}
	
	public boolean canModifyFlag(boolean isOp, boolean isOwner, boolean isMember) {
		if (isOp) {
			return true;
		} else if (isOwner && (this.perm == FlagPerm.OWNER || this.perm == FlagPerm.MEMBER)) {
			return true;
		} else if (isMember && this.perm == FlagPerm.MEMBER) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (!isNull()) {
			Object value = getValue();
			if (value != null) {
				if (this.type == FlagType.BOOLEAN) {
					return String.valueOf(value);
				} else if (this.type == FlagType.INT) {
					return String.valueOf(value);
				} else if (this.type == FlagType.LONG) {
					return String.valueOf(value);
				} else if (this.type == FlagType.DOUBLE) {
					return String.valueOf(value);
				} else if (this.type == FlagType.STRING) {
					return String.valueOf(value);
				} else if (this.type == FlagType.LIST_STRING) {
					return Arrays.toString((String[])value);
				} else if (this.type == FlagType.PERMISSION) {
					return Arrays.toString(((Permission)value).toArray());
				}
			}
		}
		return "NULL";
	}
	
	public boolean isEqual(Object obj) {
		if (!isNull()) {
			if (obj instanceof String && getString() != null) {
				String flag = getString();
				String value = (String)obj;
				return flag.equals(value);
			} else if (obj instanceof Integer && getInt() != null) {
				Integer flag = getInt();
				Integer value = (Integer)obj;
				return flag == value;
			} else if (obj instanceof Long && getLong() != null) {
				Long flag = getLong();
				Long value = (Long)obj;
				return flag == value;
			} else if (obj instanceof Double && getDouble() != null) {
				Double flag = getDouble();
				Double value = (Double)obj;
				return flag == value;
			} else if (obj instanceof Boolean && getBoolean() != null) {
				Boolean flag = getBoolean();
				Boolean value = (Boolean)obj;
				return flag == value;
			} else if (obj instanceof String[] && getStringArray() != null) {
				String[] flag = getStringArray();
				String[] value = (String[])obj;
				return Arrays.equals(flag, value);
			} else if (obj instanceof Permission && getPermission() != null) {
				return java.util.Objects.equals((Permission)obj, getPermission());
			}
		}
		return false;
	}
	
	public String getString() {
		if (getType() == FlagType.STRING && getValue() instanceof String) {
			return (String)getValue();
		}
		return null;
	}
	
	public Integer getInt() {
		if (getType() == FlagType.INT && getValue() instanceof Integer) {
			return (Integer)getValue();
		}
		return null;
	}
	
	public Long getLong() {
		if (getType() == FlagType.LONG && getValue() instanceof Long) {
			return (Long)getValue();
		}
		return null;
	}
	
	public Double getDouble() {
		if (getType() == FlagType.DOUBLE && getValue() instanceof Double) {
			return (Double)getValue();
		}
		return null;
	}
	
	public Boolean getBoolean() {
		if (getType() == FlagType.BOOLEAN && getValue() instanceof Boolean) {
			return (Boolean) getValue();
		}
		return null;
	}
	
	public String[] getStringArray() {
		if (getType() == FlagType.LIST_STRING && getValue() instanceof String[]) {
			return (String[])getValue();
		}
		return new String[] {};
	}
	
	public Permission getPermission() {
		if (getType() == FlagType.PERMISSION && getValue() instanceof Permission) {
			return (Permission)getValue();
		}
		return null;
	}
}

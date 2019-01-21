# WeatherForecast
[{"key_value":"data_value"}]

String data = jsonObject.getString("key_value"); //ERROR

When the value corresponding the key could be "NULL", we use optString which gives Default value if value found is null.

String data = jsonObject.optString("key_value", null);

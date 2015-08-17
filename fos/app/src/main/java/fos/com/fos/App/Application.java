package fos.com.fos.App;

import com.parse.Parse;

public class Application extends android.app.Application {
  @Override public void onCreate() {
    super.onCreate();
    Parse.initialize(this, "0YKQzgC4FATWuo8lF7sIUqj29I44qc6MgGTlvnvg",
        "EcpZOyo2miRxTgpqXTzWNkpL85Op6bXPPydWK1FG");
  }
}

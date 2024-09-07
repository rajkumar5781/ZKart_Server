package connectors;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.cql.*;
 
import static java.lang.System.out;

public class CassandraConnection
{
   private Cluster cluster;
 
   private Session session;
 
public void connect()
   {
	try {
      final String ipAddress = "127.0.0.1";
	  final int port = 9042;
	  final String keyspace = "zkart";
      this.cluster = Cluster.builder().addContactPoint(ipAddress).withPort(port).build();
     
//      final Metadata metadata = cluster.getMetadata();
//      out.printf("Connected to cluster: %s\n", metadata.getClusterName());
//      for (final Host host : metadata.getAllHosts())
//      {
//         out.printf("Datacenter: %s; Rack: %s\n",
//            host.getDatacenter(), host.getRack());
//      }
      this.session = cluster.connect(keyspace);
	}
	catch (Exception e) {
		System.out.print(e.getMessage());
	}
   }
public void connectdb(String seeds, int port) {
    this.cluster = Cluster.builder().addContactPoint(seeds).withPort(port).build();
    final Metadata metadata = cluster.getMetadata();

    for (final Host host : metadata.getAllHosts()) {
        System.out.println("driver version " + host.getCassandraVersion());
    }

    this.session = cluster.connect();
}
 
   public Session getSession()
   {
      return this.session;
   }
 
   public void close()
   {
      cluster.close();
   }
}
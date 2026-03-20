package org.acme;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Path("/toss")
public class TossCoinResource {

    private final Counter heads;
    private final Counter tails;
    private final Counter total;

    public TossCoinResource(MeterRegistry registry) {
        // Inicializamos los tres contadores
        this.heads = Counter.builder("coin_toss_heads")
                .description("Total de caras")
                .register(registry);
        
        this.tails = Counter.builder("coin_toss_tails")
                .description("Total de cruces")
                .register(registry);
                
        this.total = Counter.builder("coin_toss_total")
                .description("Total de lanzamientos")
                .register(registry);
    }

    @GET
    public Response tossACoin() {
        total.increment();
        Random rand = new Random();
        
        if (rand.nextBoolean()) {
            heads.increment();
            return Response.ok("Resultado: CARA (HEADS)").build();
        } else {
            tails.increment();
            return Response.ok("Resultado: CRUZ (TAILS)").build();
        }
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStats() {
        Map<String, Double> stats = new HashMap<>();
        stats.put("total", total.count());
        stats.put("heads", heads.count());
        stats.put("tails", tails.count());
        
        return Response.ok(stats).build();
    }
}

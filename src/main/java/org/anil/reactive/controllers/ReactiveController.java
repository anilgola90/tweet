package org.anil.reactive.controllers;


import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.anil.reactive.entity.CarEntity;
import org.anil.reactive.entity.CarRepository;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReactiveController {
    private final CarRepository carRepository;
    private final ConnectionFactory connectionFactory;
      public Mono<Connection> getConnection(){
          return  Mono.from(connectionFactory.create());
      }
    @GetMapping("/saveviafactory")
    public Flux<String>  saveviafactory(){
        Flux<String> stringFlux = getConnection().
                flatMapMany(conn -> {
                            return conn.createStatement("INSERT INTO car(brand,kilowatt) values('amazing',343)")
                                    .returnGeneratedValues("id")
                                    .execute()
                                    ;
                        }
                ).flatMap(result -> result.map((row, rowMetaData) -> row.get("id").toString()));
        return stringFlux;
    }

    @GetMapping("/saveintransaction")
    public Flux<String>  saveviafactoryTransaction(){
        ReactiveTransactionManager tm = new R2dbcTransactionManager(connectionFactory);
        TransactionalOperator rxtx = TransactionalOperator.create(tm);
        Flux<String> execute = rxtx.execute(action ->
                getConnection().
                    flatMapMany(conn -> {
                        return conn.createStatement("INSERT INTO car(brand,kilowatt) values('amazing',343)")
                                .returnGeneratedValues("id")
                                .execute();
                        }
                    ).flatMap(result -> result.map((row, rowMetaData) -> row.get("id").toString()))
        );
        return execute;
    }

    @GetMapping("/savecarviar2dbcrepository")
    public Mono<CarEntity> saveCard(){
        var car =  CarEntity.builder()
                .brand("b").kilowatt(12).build();
        Mono<CarEntity> save = carRepository.save(car);
        return save;
    }


    @GetMapping("/userdetails")
    public Mono<UserDetails> getuserdetails(ServerWebExchange serverRequest){
        Mono<UserDetails> map = serverRequest.getPrincipal().map(p -> ((UserDetails) (((Authentication) p).getPrincipal())));
        return map;
    }
}



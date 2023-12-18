package ru.otus.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = ["ru.otus.mongo"])
class MongoConfig : AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
        return "test"
    }

    override fun mongoClient(): MongoClient {
        val host = System.getenv("MONGO_HOST") ?: "localhost"
        val connectionString = ConnectionString("mongodb://$host:27017/test")
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    public override fun getMappingBasePackages(): Collection<String> {
        return setOf("ru.otus.mongo")
    }
}

data class User(
    val name: String
)


interface UserRepository : MongoRepository<User, String> { //
}
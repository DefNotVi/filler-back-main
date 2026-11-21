package cl.back.filler.service;

import cl.back.filler.dto.ConsumerGroupDTO;
import cl.back.filler.dto.TopicInfoDTO;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KafkaAdminService {

    private static final Logger log = LoggerFactory.getLogger(KafkaAdminService.class);
    private final KafkaAdmin kafkaAdmin;

    public KafkaAdminService(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    public void crearTopico(String nombre, int particiones, short replicas, String retentionMs) throws Exception {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            Map<String, String> configs = new HashMap<>();
            configs.put(TopicConfig.RETENTION_MS_CONFIG, retentionMs != null ? retentionMs : "604800000");
            configs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);

            NewTopic nuevoTopico = new NewTopic(nombre, particiones, replicas).configs(configs);
            client.createTopics(Collections.singletonList(nuevoTopico)).all().get();
            log.info("[KAFKA ADMIN] Tópico creado: {} (Particiones: {}, Réplicas: {})", nombre, particiones, replicas);
        }
    }

    public List<TopicInfoDTO> listarTopicos() throws Exception {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            Set<String> nombres = client.listTopics().names().get().stream()
                    .filter(name -> !name.startsWith("__"))
                    .collect(Collectors.toSet());

            if (nombres.isEmpty()) {
                return Collections.emptyList();
            }

            Map<String, TopicDescription> descripciones = client.describeTopics(nombres).allTopicNames().get();

            List<TopicInfoDTO> resultado = new ArrayList<>();
            for (TopicDescription desc : descripciones.values()) {
                int reps = desc.partitions().isEmpty() ? 0 : desc.partitions().get(0).replicas().size();
                resultado.add(new TopicInfoDTO(desc.name(), desc.partitions().size(), reps));
            }
            return resultado;
        }
    }

    public void modificarTopico(String nombre, Integer nuevasParticiones, String nuevaRetencionMs) throws Exception {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            if (nuevasParticiones != null && nuevasParticiones > 0) {
                Map<String, NewPartitions> updatePartitions = Collections.singletonMap(
                        nombre, NewPartitions.increaseTo(nuevasParticiones)
                );
                client.createPartitions(updatePartitions).all().get();
                log.info("[KAFKA ADMIN] Particiones de '{}' aumentadas a {}", nombre, nuevasParticiones);
            }

            if (nuevaRetencionMs != null && !nuevaRetencionMs.isBlank()) {
                ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, nombre);
                ConfigEntry entry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, nuevaRetencionMs);
                Map<ConfigResource, Collection<AlterConfigOp>> configs = Collections.singletonMap(
                        resource,
                        Collections.singletonList(new AlterConfigOp(entry, AlterConfigOp.OpType.SET))
                );
                client.incrementalAlterConfigs(configs).all().get();
                log.info("[KAFKA ADMIN] Retención de '{}' actualizada a {} ms", nombre, nuevaRetencionMs);
            }
        }
    }

    public void eliminarTopico(String nombre) throws Exception {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            client.deleteTopics(Collections.singletonList(nombre)).all().get();
            log.info("[KAFKA ADMIN] Tópico eliminado: {}", nombre);
        }
    }

    public List<ConsumerGroupDTO> listarConsumerGroups() throws Exception {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            List<String> groupIds = client.listConsumerGroups().all().get().stream()
                    .map(ConsumerGroupListing::groupId)
                    .collect(Collectors.toList());

            if (groupIds.isEmpty()) {
                return Collections.emptyList();
            }

            Map<String, ConsumerGroupDescription> descripciones = client.describeConsumerGroups(groupIds).all().get();

            List<ConsumerGroupDTO> grupos = new ArrayList<>();
            for (ConsumerGroupDescription desc : descripciones.values()) {
                grupos.add(new ConsumerGroupDTO(desc.groupId(), desc.state().toString(), desc.members().size()));
            }
            return grupos;
        }
    }
}
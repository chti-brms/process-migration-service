/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.processmigration.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.kie.server.api.model.admin.MigrationReportInstance;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name = "migration_reports", indexes = {@Index(columnList = "migration_id")})
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@Getter
@Setter
public class MigrationReport extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "migRepIdSeq")
    @SequenceGenerator(name = "migRepIdSeq", sequenceName = "MIG_REP_ID_SEQ")
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "migration_id")
    private Long migrationId;

    @Column(name = "process_instance_id")
    private Long processInstanceId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "success") // successful is a reserved word in Oracle DB
    private Boolean successful;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "log")
    @Lob
    @CollectionTable(
            name = "migration_report_logs",
            joinColumns = @JoinColumn(name = "report_id")
    )
    private List<String> logs;

    @Transactional
    public static List<MigrationReport> listByMigrationId(Long id) {
        return list("migrationId", id);
    }

    public MigrationReport() {
    }

    public MigrationReport(Long migrationId, MigrationReportInstance reportInstance) {
        this.migrationId = migrationId;
        this.processInstanceId = reportInstance.getProcessInstanceId();
        if (reportInstance.getStartDate() != null) {
            this.startDate = reportInstance.getStartDate().toInstant();
        }
        if (reportInstance.getEndDate() != null) {
            this.endDate = reportInstance.getEndDate().toInstant();
        }
        this.successful = reportInstance.isSuccessful();
        this.logs = new ArrayList<>(reportInstance.getLogs());
    }

}

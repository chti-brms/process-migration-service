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

package org.kie.processmigration.service;

import java.util.List;

import org.kie.processmigration.model.Migration;
import org.kie.processmigration.model.MigrationDefinition;
import org.kie.processmigration.model.MigrationReport;
import org.kie.processmigration.model.MigrationReportDto;
import org.kie.processmigration.model.exceptions.InvalidMigrationException;
import org.kie.processmigration.model.exceptions.MigrationNotFoundException;
import org.kie.processmigration.model.exceptions.ReScheduleException;

public interface MigrationService {

    Migration get(Long id) throws MigrationNotFoundException;

    List<MigrationReportDto> getResults(Long id) throws MigrationNotFoundException;

    List<Migration> findAll();

    Migration submit(MigrationDefinition definition) throws InvalidMigrationException;

    Migration update(Long id, MigrationDefinition migration) throws ReScheduleException, MigrationNotFoundException, InvalidMigrationException;

    Migration delete(Long id) throws MigrationNotFoundException;

    Migration migrate(Migration migration) throws InvalidMigrationException;

    List<Migration> findPending();

    MigrationReport getReport(Long id);
}

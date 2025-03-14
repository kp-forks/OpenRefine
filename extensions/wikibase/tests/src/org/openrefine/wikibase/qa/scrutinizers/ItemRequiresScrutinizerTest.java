
package org.openrefine.wikibase.qa.scrutinizers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.testng.annotations.Test;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.implementation.StatementImpl;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

import org.openrefine.wikibase.qa.ConstraintFetcher;
import org.openrefine.wikibase.testing.TestingData;
import org.openrefine.wikibase.updates.ItemEditBuilder;
import org.openrefine.wikibase.updates.TermedStatementEntityEdit;

public class ItemRequiresScrutinizerTest extends ScrutinizerTest {

    public static final String ITEM_REQUIRES_CONSTRAINT_QID = "Q21503247";
    public static final String ITEM_REQUIRES_PROPERTY_PID = "P2306";
    public static final String ITEM_OF_PROPERTY_CONSTRAINT_PID = "P2305";

    public static PropertyIdValue propertyIdValue = Datamodel.makeWikidataPropertyIdValue("P157");
    public static ItemIdValue itemValue = Datamodel.makeWikidataItemIdValue("Q3187975");
    public static ItemIdValue entityIdValue = Datamodel.makeWikidataItemIdValue(ITEM_REQUIRES_CONSTRAINT_QID);
    public static PropertyIdValue propertyParameterPID = Datamodel.makeWikidataPropertyIdValue(ITEM_REQUIRES_PROPERTY_PID);
    public static PropertyIdValue propertyParameterValue = Datamodel.makeWikidataPropertyIdValue("P1196");
    public static PropertyIdValue itemParameterPID = Datamodel.makeWikidataPropertyIdValue(ITEM_OF_PROPERTY_CONSTRAINT_PID);
    public static Value requiredValue = Datamodel.makeWikidataItemIdValue("Q149086");

    @Override
    public EditScrutinizer getScrutinizer() {
        return new ItemRequiresScrutinizer();
    }

    @Test
    public void testExistingItemTrigger() {
        ItemIdValue idA = TestingData.existingId;
        Snak mainSnak = Datamodel.makeValueSnak(propertyIdValue, itemValue);
        Statement statement = new StatementImpl("P157", mainSnak, idA);
        TermedStatementEntityEdit updateA = new ItemEditBuilder(idA)
                .addStatement(add(statement))
                .addContributingRowId(123)
                .build();

        Snak qualifierSnak1 = Datamodel.makeValueSnak(propertyParameterPID, propertyParameterValue);
        Snak qualifierSnak2 = Datamodel.makeValueSnak(itemParameterPID, requiredValue);
        List<SnakGroup> constraintQualifiers = makeSnakGroupList(qualifierSnak1, qualifierSnak2);
        List<Statement> constraintDefinitions = constraintParameterStatementList(entityIdValue, constraintQualifiers);

        ConstraintFetcher fetcher = mock(ConstraintFetcher.class);
        when(fetcher.getConstraintsByType(propertyIdValue, ITEM_REQUIRES_CONSTRAINT_QID)).thenReturn(constraintDefinitions);
        setFetcher(fetcher);

        scrutinize(updateA);
        assertWarningsRaised(ItemRequiresScrutinizer.existingItemRequireValueswithSuggestedValueType);
    }

    @Test
    public void testWrongValue() {
        ItemIdValue idA = TestingData.existingId;
        Snak mainSnak = Datamodel.makeValueSnak(propertyIdValue, itemValue);
        Statement statement = new StatementImpl("P157", mainSnak, idA);
        Snak requiredPropertySnak = Datamodel.makeValueSnak(propertyParameterValue, itemValue);
        Statement requiredStatement = new StatementImpl("P1196", requiredPropertySnak, idA);
        TermedStatementEntityEdit updateA = new ItemEditBuilder(idA)
                .addStatement(add(statement))
                .addStatement(add(requiredStatement))
                .addContributingRowId(123)
                .build();

        Snak qualifierSnak1 = Datamodel.makeValueSnak(propertyParameterPID, propertyParameterValue);
        Snak qualifierSnak2 = Datamodel.makeValueSnak(itemParameterPID, requiredValue);
        List<SnakGroup> constraintQualifiers = makeSnakGroupList(qualifierSnak1, qualifierSnak2);
        List<Statement> constraintDefinitions = constraintParameterStatementList(entityIdValue, constraintQualifiers);

        ConstraintFetcher fetcher = mock(ConstraintFetcher.class);
        when(fetcher.getConstraintsByType(propertyIdValue, ITEM_REQUIRES_CONSTRAINT_QID)).thenReturn(constraintDefinitions);
        setFetcher(fetcher);

        scrutinize(updateA);
        assertWarningsRaised(ItemRequiresScrutinizer.existingItemRequireValueswithSuggestedValueType);
    }

    @Test
    public void testCorrectValue() {
        ItemIdValue idA = TestingData.existingId;
        Snak mainSnak = Datamodel.makeValueSnak(propertyIdValue, itemValue);
        Statement statement = new StatementImpl("P157", mainSnak, idA);
        Snak requiredPropertySnak = Datamodel.makeValueSnak(propertyParameterValue, requiredValue);
        Statement requiredStatement = new StatementImpl("P1196", requiredPropertySnak, idA);
        TermedStatementEntityEdit updateA = new ItemEditBuilder(idA)
                .addStatement(add(statement))
                .addStatement(add(requiredStatement))
                .addContributingRowId(123)
                .build();

        Snak qualifierSnak1 = Datamodel.makeValueSnak(propertyParameterPID, propertyParameterValue);
        Snak qualifierSnak2 = Datamodel.makeValueSnak(itemParameterPID, requiredValue);
        List<SnakGroup> constraintQualifiers = makeSnakGroupList(qualifierSnak1, qualifierSnak2);
        List<Statement> constraintDefinitions = constraintParameterStatementList(entityIdValue, constraintQualifiers);

        ConstraintFetcher fetcher = mock(ConstraintFetcher.class);
        when(fetcher.getConstraintsByType(propertyIdValue, ITEM_REQUIRES_CONSTRAINT_QID)).thenReturn(constraintDefinitions);
        setFetcher(fetcher);

        scrutinize(updateA);
        assertNoWarningRaised();
    }

    @Test
    public void testNewItemTrigger() {
        ItemIdValue idA = TestingData.newIdA;
        Snak mainSnak = Datamodel.makeValueSnak(propertyIdValue, itemValue);
        Statement statement = new StatementImpl("P157", mainSnak, idA);
        TermedStatementEntityEdit updateA = new ItemEditBuilder(idA)
                .addStatement(add(statement))
                .addContributingRowId(123)
                .build();

        Snak qualifierSnak1 = Datamodel.makeValueSnak(propertyParameterPID, propertyParameterValue);
        Snak qualifierSnak2 = Datamodel.makeValueSnak(itemParameterPID, requiredValue);
        List<SnakGroup> constraintQualifiers = makeSnakGroupList(qualifierSnak1, qualifierSnak2);
        List<Statement> constraintDefinitions = constraintParameterStatementList(entityIdValue, constraintQualifiers);

        ConstraintFetcher fetcher = mock(ConstraintFetcher.class);
        when(fetcher.getConstraintsByType(propertyIdValue, ITEM_REQUIRES_CONSTRAINT_QID)).thenReturn(constraintDefinitions);
        setFetcher(fetcher);

        scrutinize(updateA);
        assertWarningsRaised(ItemRequiresScrutinizer.newItemRequireValueswithSuggestedValueType);
    }

    @Test
    public void testExistingItemNoIssue() {
        ItemIdValue id = TestingData.existingId;
        ValueSnak mainSnak1 = Datamodel.makeValueSnak(propertyIdValue, itemValue);
        ValueSnak mainSnak2 = Datamodel.makeValueSnak(propertyParameterValue, requiredValue);
        Statement statement1 = new StatementImpl("P157", mainSnak1, id);
        Statement statement2 = new StatementImpl("P1196", mainSnak2, id);
        TermedStatementEntityEdit update = new ItemEditBuilder(id)
                .addStatement(add(statement1))
                .addStatement(add(statement2))
                .addContributingRowId(123)
                .build();

        Snak qualifierSnak1 = Datamodel.makeValueSnak(propertyParameterPID, propertyParameterValue);
        Snak qualifierSnak2 = Datamodel.makeValueSnak(itemParameterPID, requiredValue);
        List<SnakGroup> constraintQualifiers = makeSnakGroupList(qualifierSnak1, qualifierSnak2);
        List<Statement> constraintDefinitions = constraintParameterStatementList(entityIdValue, constraintQualifiers);

        ConstraintFetcher fetcher = mock(ConstraintFetcher.class);
        when(fetcher.getConstraintsByType(propertyIdValue, ITEM_REQUIRES_CONSTRAINT_QID)).thenReturn(constraintDefinitions);

        setFetcher(fetcher);
        scrutinize(update);
        assertNoWarningRaised();
    }
}

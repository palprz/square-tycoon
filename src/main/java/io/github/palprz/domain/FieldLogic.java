package io.github.palprz.domain;

import com.vaadin.flow.component.html.Div;
import io.github.palprz.builders.BoardGenerator;
import io.github.palprz.components.NotificationInfo;
import io.github.palprz.components.NotificationWarning;
import io.github.palprz.storage.Storage;
import io.github.palprz.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

public class FieldLogic {

    private static Logger LOG = LoggerFactory.getLogger(FieldLogic.class);

    @Inject
    private BoardLogic boardLogic;

    @Inject
    private BoardGenerator boardGenerator;

    /**
     * Set chosen by end-user production to the field. It will include updating field metadata, CSS and money
     * calculations.
     *
     * @param field      Updated field
     * @param production Chosen production
     */
    public void provideProductionToField(Field field, ProductionEnum production) {
        Div title = (Div) field.getElement().getChildren().findFirst().get();
        if (production == ProductionEnum.NONE) {
            title.setText("");
        } else {
            title.setText(production.getDisplayName());
        }
        FieldMetadata fieldMeta = field.getFieldMetadata();

        ProductionEnum oldProduction = fieldMeta.getProduction();
        fieldMeta.setProduction(production);

        if (fieldMeta.getProduction() == ProductionEnum.WATER) {
            fieldMeta.setHasRequireProducts(true);
        }

        Map<DirectionEnum, Field> fieldsAround = boardLogic.getFieldsAroundByDirection(
                fieldMeta.getRow(),
                fieldMeta.getColumn());

        this.checkFieldConnections(field, fieldsAround, oldProduction);
        /*
         * If has got require products then it's possible that the rest around fields started producing their own
         * products so run chain to check it
         */
        LOG.debug("Init running chain for: {}", field);
        this.runChainForCheckRequireProducts(field, fieldsAround, new ArrayList<>(), oldProduction);

        NotificationInfo.show("Set production of " + production.getName());
    }

    /**
     * Check if provided field has got any connection to the next (in cross) fields. Field require products and is able
     * to provide products, so it can connect with other fields + market can connect with any other field which is
     * providing income.
     *
     * @param field         Updated field
     * @param fieldsAround  Fields around (in cross) next to the updated field
     * @param oldProduction Old production of the field
     */
    private void checkFieldConnections(Field field, Map<DirectionEnum, Field> fieldsAround, ProductionEnum oldProduction) {
        ProductionEnum newProduction = field.getFieldMetadata().getProduction();
        for (Map.Entry<DirectionEnum, Field> entry : fieldsAround.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            FieldMetadata nextFieldMeta = entry.getValue().getFieldMetadata();
            Position nextFieldPosition = new Position(nextFieldMeta.getRow(), nextFieldMeta.getColumn());
            Field nextField = Storage.get().getBoard().get(nextFieldPosition);

            if (newProduction == ProductionEnum.NONE) {
                // when we will cancel the production
                this.disconnectFieldsByCSS(field, nextField, entry.getKey());
                LOG.debug("Set to nothing - old production: {}", oldProduction);
                if (oldProduction == ProductionEnum.MARKET) {
                    // check if after removing market there is still a market connected to the next field
                    boolean marketNextField = boardLogic.isMarketNextField(nextField);
                    LOG.debug("Has next field got market? '{}' for field {}", marketNextField, nextField);
                    nextField.getFieldMetadata().setMarketConnected(marketNextField);
                }
                continue;
            }

            ProductionEnum nextFieldProduction = nextFieldMeta.getProduction();
            if (nextFieldProduction == ProductionEnum.NONE) {
                // no production - nothing to connect to
                continue;
            }

            if (newProduction == ProductionEnum.MARKET) {
                // market to field
                if (nextFieldProduction.getIncome() != 0 && nextFieldProduction != ProductionEnum.MARKET) {
                    this.connectFieldsByCSS(field, nextField, entry.getKey());
                    nextField.getFieldMetadata().setMarketConnected(true);
                }
                continue;
            }

            // field to market
            if (newProduction.getIncome() != 0 && nextFieldProduction == ProductionEnum.MARKET) {
                this.connectFieldsByCSS(field, nextField, entry.getKey());
                field.getFieldMetadata().setMarketConnected(true);
                continue;
            }

            // field to field

            // is existing field providing any require product for a new field?
            // and
            // is existing field requiring any providing product from a new field?
            if (!(Collections.disjoint(newProduction.getRequireProducts(), nextFieldProduction.getProvideProducts())
                    && Collections.disjoint(newProduction.getProvideProducts(), nextFieldProduction.getRequireProducts()))) {
                this.connectFieldsByCSS(field, nextField, entry.getKey());
            }
        }
    }

    /**
     * Initialize check for require products for the provided fields and for the fields around which haven't been check
     * already.
     *
     * @param field                Currently checking field
     * @param fieldsAround         Fields next (in cross) to the checking field
     * @param alreadyCheckedFields List with already checked fields
     * @param oldProduction        Old production of the updated at the beginning field
     */
    private void runChainForCheckRequireProducts(Field field, Map<DirectionEnum, Field> fieldsAround,
                                                 List<Field> alreadyCheckedFields, ProductionEnum oldProduction) {
        LOG.debug("Add to the check list with fields: {}", field);
        alreadyCheckedFields.add(field);

        this.checkRequireProducts(field, fieldsAround, oldProduction);
        for (Map.Entry<DirectionEnum, Field> entry : fieldsAround.entrySet()) {
            Field nextField = entry.getValue();
            if (nextField == null) {
                // edge of the board
                continue;
            }

            if (nextField.getFieldMetadata().getProduction() == ProductionEnum.NONE) {
                // if nothing produce then it's pointless to check it
                LOG.debug("No production provided: {}", nextField);
                continue;
            }

            if (alreadyCheckedFields.contains(nextField)) {
                //already checked field in this chain - skip it
                LOG.debug("Already checked field: {}", nextField);
                continue;
            }

            Map<DirectionEnum, Field> recurrentFieldsAround = boardLogic.getFieldsAroundByDirection(
                    nextField.getFieldMetadata().getRow(),
                    nextField.getFieldMetadata().getColumn());

            this.runChainForCheckRequireProducts(nextField, recurrentFieldsAround, alreadyCheckedFields, oldProduction);
        }

        LOG.debug("Done checking fields around for: {}", field);
    }

    /**
     * Check list with require products for provided field and update fieldmetadata if all require products are provided
     * by the fields next to the provided field.
     *
     * @param field         Provided field to be check
     * @param fieldsAround  Fields around (in cross) next to the provided field
     * @param oldProduction Old production of provided field
     */
    private void checkRequireProducts(Field field, Map<DirectionEnum, Field> fieldsAround, ProductionEnum oldProduction) {
        LOG.debug("Check require products for field: {}", field);
        FieldMetadata fieldMeta = field.getFieldMetadata();
        List<ProductEnum> requireProducts = fieldMeta.getProduction().getRequireProducts();
        List<ProductEnum> providedProductsFromFieldsAround = new ArrayList<>();

        for (Map.Entry<DirectionEnum, Field> entry : fieldsAround.entrySet()) {
            Field fieldAround = entry.getValue();
            if (fieldAround == null) {
                continue;
            }

            FieldMetadata fieldAroundMeta = fieldAround.getFieldMetadata();
            if (fieldAroundMeta.getProduction() == ProductionEnum.NONE) {
                // skip no production field
                continue;
            }

            List<ProductEnum> deliveredProducts = fieldAroundMeta.getProvidedProducts();
            boolean stillWorkingProperly = true;
            if (fieldMeta.getProduction() == ProductionEnum.NONE) {
                // remove old production to see if it's still valid production for the next field
                for (ProductEnum oldProduct : oldProduction.getProvideProducts()) {
                    LOG.debug("Removing product '{}' due to broken production chain from the list of provided products: {}",
                            oldProduct, deliveredProducts);
                    deliveredProducts.remove(oldProduct);
                }
                stillWorkingProperly = deliveredProducts.containsAll(fieldAroundMeta.getProduction().getRequireProducts());
            }

            LOG.debug("Still has got require products? {}, {}",
                    fieldAround.getFieldMetadata().getProduction(),
                    stillWorkingProperly);

            if (fieldAround.getFieldMetadata().isHasRequireProducts() && stillWorkingProperly) {
                providedProductsFromFieldsAround.addAll(fieldAround.getFieldMetadata().getProduction().getProvideProducts());
            }
        }

        boolean hasRequireProducts = providedProductsFromFieldsAround.containsAll(requireProducts);
        LOG.debug("Require products: {}, providing products {}, has got necessary products? {}",
                requireProducts, providedProductsFromFieldsAround, hasRequireProducts);

        fieldMeta.setProvidedProducts(providedProductsFromFieldsAround);
        fieldMeta.setHasRequireProducts(hasRequireProducts);

        if (!hasRequireProducts) {
            NotificationWarning.show("Not able to produce " + fieldMeta.getProduction().getName()
                    + " - missing some stuff from: " + requireProducts);
        }
    }

    private void connectFieldsByCSS(Field field, Field nextField, DirectionEnum nextFieldDirection) {
        // the side of the square connected with the next field
        field.getElement().addClassName("connected-" + nextFieldDirection.getName() + "-field");
        // the opposite side of the square
        nextField.getElement().addClassName("connected-" + nextFieldDirection.getOppositeDirection() + "-field");
    }

    private void disconnectFieldsByCSS(Field field, Field nextField, DirectionEnum nextFieldDirection) {
        // the side of the square disconnected with the next field
        field.getElement().removeClassName("connected-" + nextFieldDirection.getName() + "-field");
        // the opposite side of the square
        nextField.getElement().removeClassName("connected-" + nextFieldDirection.getOppositeDirection() + "-field");
    }

    public void buyField(Field field) {
        LOG.debug("Buying field...");
        FieldMetadata fieldMetadata = field.getFieldMetadata();

        // Check money status
        int availableMoney = Storage.get().getAvailableMoney();
        if (availableMoney < fieldMetadata.getFieldPrice()) {
            NotificationWarning.show("Not enough money to buy this field");
            return;
        }

        // Update money balance
        availableMoney = availableMoney - fieldMetadata.getFieldPrice();
        Storage.get().setAvailableMoney(availableMoney);

        this.makeFieldAvailable(field);

        // Make visible next fields and dimmed them
        Map<Position, Field> fieldsAround = boardLogic.getFieldsAround(fieldMetadata.getRow(), fieldMetadata.getColumn());

        LOG.debug("Found fields around: {}", fieldsAround);
        for (Map.Entry<Position, Field> entry : fieldsAround.entrySet()) {
            Field fieldAround = entry.getValue();
            if (fieldAround == null) {
                LOG.debug("Field outside the board");
                continue;
            }

            if (!fieldAround.getFieldMetadata().isDimmed()) {
                LOG.debug("Already owned field: {}", fieldAround);
                continue;
            }

            fieldAround.getFieldMetadata().setVisible(true);
            Div fieldAroundElement = fieldAround.getElement();
            fieldAroundElement.removeClassName("not-visible");
            fieldAroundElement.addClassName("dimmed");

            Div title = (Div) fieldAroundElement.getChildren().findFirst().get();
            title.setText("Price: $" + fieldAround.getFieldMetadata().getFieldPrice());
            title.addClassNames("field-title", "field-price");
        }

        NotificationInfo.show("Bought a field");
        LOG.debug("Bought field!");
    }

    private void makeFieldAvailable(Field field) {
        field.getFieldMetadata().setDimmed(false);
        Div fieldElement = field.getElement();
        fieldElement.removeClassName("dimmed");
        Div title = (Div) fieldElement.getChildren().findFirst().get();
        title.setText("");
        title.removeClassName("field-price");
    }

}

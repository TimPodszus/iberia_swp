package de.uol.swp.server.region.management;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.region.data.IRegion;

import java.util.List;

public class RegionManagement implements IRegionManagement {
    public int reduceWaterTreatments(IGame game, ICity city, int amount) throws RegionManagementException {
        List<IRegion> regions = game.getRegionRepository()
                                    .getRegionsByCityName(city.getName());
        int totalWaterTreatments = regions.stream()
                                          .mapToInt(IRegion::getWaterTreatments)
                                          .sum();

        if (totalWaterTreatments >= amount) {
            decreaseWaterTreatmentsInRegions(regions, amount);
            return 0;
        } else {
            decreaseWaterTreatmentsInAllRegions(regions);
            return amount - totalWaterTreatments;
        }
    }

    private void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException {
        for (IRegion region : regions) {
            int waterTreatments = region.getWaterTreatments();
            if (waterTreatments >= amount) {
                region.decreaseWaterTreatments(amount);
                return;
            } else {
                region.decreaseWaterTreatments(waterTreatments);
                amount -= waterTreatments;
            }
        }
    }

    private void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException {
        for (IRegion region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
        }
    }
}

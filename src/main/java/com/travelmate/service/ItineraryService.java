package com.travelmate.service;

import com.travelmate.dto.ItineraryItemDTO;
import com.travelmate.entity.ItineraryItem;
import com.travelmate.entity.Trip;
import com.travelmate.entity.User;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.exception.UnauthorizedException;
import com.travelmate.repository.ItineraryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    private final ItineraryItemRepository itineraryItemRepository;

    public ItineraryService(ItineraryItemRepository itineraryItemRepository) {
        this.itineraryItemRepository = itineraryItemRepository;
    }

    public List<ItineraryItemDTO> getItineraryForTrip(Trip trip) {
        return itineraryItemRepository.findByTripIdOrderByDayNumberAscIdAsc(trip.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItineraryItem> generateInitialItinerary(Trip trip) {
        long days = ChronoUnit.DAYS.between(trip.getDepartureDate(), trip.getReturnDate()) + 1;
        if (days < 1) days = 1;
        if (days > 14) days = 14; // Reasonable limit for initial auto generation

        String destLower = trip.getDestination().toLowerCase();
        String pref = trip.getTravelPreference() != null ? trip.getTravelPreference().toLowerCase() : "relaxation";

        List<ItineraryItem> generated = new ArrayList<>();

        for (int day = 1; day <= days; day++) {
            generated.addAll(createDayActivities(trip, day, destLower, pref));
        }

        return itineraryItemRepository.saveAll(generated);
    }

    private List<ItineraryItem> createDayActivities(Trip trip, int day, String dest, String pref) {
        List<ItineraryItem> dayItems = new ArrayList<>();

        if (dest.contains("goa")) {
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Arrival & Check-in at North Goa Resort", "Settle in, unpack, and enjoy refreshing coconut water by the beach.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Calangute & Baga Beach Stroll", "Explore vibrant beach shacks, relax on sunbeds, and try local Goan fish curry.", 800.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Sunset at Tito's Lane & Beachside Cafe", "Experience Goa's famous evening vibes with live acoustic music and sea breeze.", 1200.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Fort Aguada & Sinquerim Lighthouse", "Explore the 17th-century Portuguese fortress with panoramic Arabian Sea views.", 300.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Water Sports at Anjuna Beach", "Enjoy thrilling parasailing, jet skiing, or banana boat rides.", 1800.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Anjuna Flea Market & Curties Cafe", "Browse handmade souvenirs, bohemian clothing, and watch the sun dip into the sea.", 700.0));
            } else if (day == 3) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Heritage Walk in Fontainhas (Latin Quarter)", "Stroll through colorful colonial lanes, art galleries, and historic bakeries in Panaji.", 500.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Basilica of Bom Jesus & Old Goa Churches", "Visit UNESCO World Heritage sites holding the sacred relics of St. Francis Xavier.", 250.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Mandovi River Dinner Cruise", "Scenic 2-hour river cruise with traditional Goan folk performances and dinner.", 1500.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "South Goa Palolem Beach Relaxation", "Tranquil morning swimming in crescent-shaped bay surrounded by coconut palms.", 450.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Spice Plantation Tour & Authentic Buffet", "Guided tour through aromatic vanilla, pepper, and cinnamon groves with buffet lunch.", 1100.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Cabo de Rama Cliff Sunset View", "Spectacular vantage point overlooking cliffs and peaceful secluded beaches.", 500.0));
            }
        } else if (dest.contains("manali")) {
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Arrival, Hotel Check-in & Acclimatization", "Warm Himalayan tea, unpack, and take in panoramic snow peak views.", 300.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Hadimba Devi Temple & Van Vihar Cedar Woods", "Visit the ancient pagoda-style wooden temple nestled inside giant deodar forest.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Mall Road Exploration & Cafe Hopping", "Sample hot momos, siddu, and shop for Kullu shawls and wooden handicrafts.", 800.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Excursion to Solang Valley", "Experience alpine thrills including paragliding, zorbing, and ropeway rides.", 2000.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Atal Tunnel Drive & Sissu Waterfall", "Drive through the engineering marvel tunnel into Lahaul valley's snow landscapes.", 1200.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Old Manali Cozy Bohemian Cafes", "Live indie music, pizza, and hot chocolate at renowned Old Manali eateries.", 900.0));
            } else if (day == 3) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Vashisht Village & Natural Hot Water Springs", "Soak in therapeutic sulphur springs and admire stone architecture.", 350.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Jogini Waterfalls Nature Trek", "Scenic 3 km hike through apple orchards and pine woods to cascade pools.", 500.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Riverside Bonfire & Stargazing", "Enjoy campfire warmth beside the roaring Beas River under clear mountain skies.", 700.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Naggar Castle Heritage Visit", "Explore medieval wood-and-stone castle with art galleries overlooking Kullu valley.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Roerich Art Gallery & Trout Fishing Lunch", "Taste fresh Himalayan rainbow trout and appreciate Himalayan mountain paintings.", 1100.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Souvenir Shopping & Traditional Dinner", "Pick up mountain honey, local jams, and warm woolen attire.", 800.0));
            }
        } else if (dest.contains("jaipur")) {
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Arrival & Royal Check-in", "Welcome drink and freshen up for exploring Rajasthan's Pink City.", 300.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "City Palace & Jantar Mantar Observatory", "Marvel at courtyards, Peacock Gate, and the world's largest stone sundial.", 900.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Hawa Mahal Facade & Wind View Cafe", "Photowalk of the iconic 953-window Palace of Winds with tea overlooking the monument.", 500.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Amber Fort & Palace Tour", "Ascend to the hilltop fortress to admire Sheesh Mahal (Mirror Palace) and ramparts.", 1200.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Panna Meena ka Kund & Jal Mahal Halt", "Stepwell photography and viewing the submerged palace floating on Man Sagar Lake.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Nahargarh Fort Sunset Point", "Watch Jaipur city glow golden at dusk from the Aravali mountain fortress.", 700.0));
            } else if (day == 3) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Albert Hall Museum & Ram Niwas Garden", "Indo-Saracenic architectural jewel showcasing royal carpets, weapons, and statues.", 500.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Johari & Bapu Bazaar Shopping", "Shop for authentic blue pottery, bandhani sarees, gemstones, and leather mojris.", 1500.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Chokhi Dhani Ethnic Cultural Village", "Immerse in Rajasthani folk dance, puppet shows, camel rides, and grand Thali feast.", 1600.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Jaigarh Fort & Jaivana Cannon", "Inspect the world's largest cannon on wheels and secret subterranean passages.", 500.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Galtaji Monkey Temple & Sun Temple", "Ancient temple complex built into mountain pass with natural sacred water tanks.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Rooftop Dinner Overlooking Illuminated Forts", "Royal dining experience savoring Laal Maas and Dal Baati Churma.", 1400.0));
            }
        } else if (dest.contains("kerala") || dest.contains("alleppey")) {
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Board Traditional Kettuvallam Houseboat", "Welcome with tender coconut and sail gently through emerald palm-fringed canals.", 2500.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Backwater Cruise & Authentic Karimeen Lunch", "Enjoy freshly prepared pearl spot fish fry, Kerala red rice, and village views.", 1000.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Sunset over Vembanad Lake & Canoe Ride", "Serene canoe ride through narrow village canals during twilight.", 600.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Marari Beach Relaxation & Fisherman Watch", "Quiet golden sand beach walk observing traditional catamarans returning with catch.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Ayurvedic Rejuvenation Massage", "Authentic 60-minute Abhyanga herbal oil massage and steam therapy.", 2000.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Kathakali & Kalaripayattu Martial Arts Show", "Mesmerizing traditional drama makeup ritual and ancient martial art spectacle.", 800.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Munnar Tea Plantation Excursion", "Walk through rolling green carpets of tea estates and mist-covered hills.", 800.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Tea Museum & Cardamom Spice Garden Tour", "Learn the art of tea crafting and sample organic spices.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Echo Point & Kundala Lake Boating", "Pedal boating surrounded by pine trees and picturesque reflections.", 700.0));
            }
        } else if (dest.contains("hyderabad")) {
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Arrival & Check-in", "Freshen up with Irani Chai and Osmania biscuits.", 300.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Historic Charminar & Laad Bazaar", "Climb the grand 1591 monument and explore world-famous lacquer bangle stalls.", 500.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Authentic Hyderabadi Dum Biryani Feast", "Savor legendary mutton biryani, double ka meetha, and mirchi ka salan at Paradise.", 900.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Golconda Fort Acoustic Tour", "Discover incredible acoustic engineering where handclaps at the gate echo at the summit.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Qutb Shahi Tombs & Chowmahalla Palace", "Admire royal coronation halls, vintage Rolls-Royce cars, and Persian chandeliers.", 800.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Hussain Sagar Lake & Buddha Statue Boat Ride", "Motorboat across the lake to the monolithic 18-meter granite Buddha statue.", 500.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Salar Jung Museum & Clock Room", "One of India's premier national museums housing treasures from around the globe.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Ramoji Film City Excursion", "Visit the world's largest film studio complex with cinematic theme sets.", 2200.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Durgam Cheruvu Cable Bridge & Lake Promenade", "Illuminated modern bridge walk and lakefront dining in HITEC City.", 1000.0));
            }
        } else {
            // Smart adaptive itinerary for any other destination
            String capDest = trip.getDestination();
            if (day == 1) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Arrival & Hotel Check-in in " + capDest, "Arrive, check into accommodation, unpack, and orient with local neighborhood.", 400.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Historic City Center & Heritage Landmarks", "Visit iconic monuments, local squares, and sample authentic regional cuisine.", 700.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Scenic Viewpoint & Sunset Promenade", "Watch sunset from the best vantage point in " + capDest + " followed by dinner.", 900.0));
            } else if (day == 2) {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Nature Trail / Adventure Highlights", "Outdoor excursion, nature park, or scenic river/mountain excursion.", 1000.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Local Cultural Museum & Art Village", "Immerse in indigenous craftsmanship, art history, and local exhibitions.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Traditional Market & Gastronomy Walk", "Shop for regional specialties, handicrafts, and street food delicacies.", 1100.0));
            } else {
                dayItems.add(new ItineraryItem(trip, day, "Morning", "Hidden Gems & Architectural Wonders", "Off-the-beaten-path temples, gardens, or architectural photography spots.", 600.0));
                dayItems.add(new ItineraryItem(trip, day, "Afternoon", "Leisure & Wellness Activities", "Relaxing cafe break, wellness spa, or boating excursion.", 900.0));
                dayItems.add(new ItineraryItem(trip, day, "Evening", "Celebratory Farewell Dinner", "Fine dining experience featuring local specialties and live cultural music.", 1500.0));
            }
        }

        return dayItems;
    }

    @Transactional
    public ItineraryItemDTO addItem(Trip trip, ItineraryItemDTO dto, User user) {
        verifyTripOwnership(trip, user);

        ItineraryItem item = new ItineraryItem(
                trip,
                dto.getDayNumber(),
                dto.getTimeOfDay().trim(),
                dto.getActivityName().trim(),
                dto.getDescription() != null ? dto.getDescription().trim() : "",
                dto.getEstimatedCost() != null ? dto.getEstimatedCost() : 0.0
        );

        ItineraryItem saved = itineraryItemRepository.save(item);
        return toDTO(saved);
    }

    @Transactional
    public ItineraryItemDTO updateItem(Trip trip, Long itemId, ItineraryItemDTO dto, User user) {
        verifyTripOwnership(trip, user);

        ItineraryItem item = itineraryItemRepository.findByIdAndTripId(itemId, trip.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + itemId));

        if (dto.getDayNumber() != null) item.setDayNumber(dto.getDayNumber());
        if (dto.getTimeOfDay() != null) item.setTimeOfDay(dto.getTimeOfDay().trim());
        if (dto.getActivityName() != null) item.setActivityName(dto.getActivityName().trim());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription().trim());
        if (dto.getEstimatedCost() != null) item.setEstimatedCost(dto.getEstimatedCost());

        ItineraryItem updated = itineraryItemRepository.save(item);
        return toDTO(updated);
    }

    @Transactional
    public void deleteItem(Trip trip, Long itemId, User user) {
        verifyTripOwnership(trip, user);

        ItineraryItem item = itineraryItemRepository.findByIdAndTripId(itemId, trip.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + itemId));

        itineraryItemRepository.delete(item);
    }

    private void verifyTripOwnership(Trip trip, User user) {
        if (!trip.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this trip's itinerary.");
        }
    }

    public ItineraryItemDTO toDTO(ItineraryItem item) {
        return new ItineraryItemDTO(
                item.getId(),
                item.getTrip().getId(),
                item.getDayNumber(),
                item.getTimeOfDay(),
                item.getActivityName(),
                item.getDescription(),
                item.getEstimatedCost()
        );
    }
}

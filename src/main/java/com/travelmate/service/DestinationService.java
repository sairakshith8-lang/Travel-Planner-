package com.travelmate.service;

import com.travelmate.entity.Destination;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.repository.DestinationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));
    }

    public List<Destination> searchDestinations(String query, String travelType, Double maxCost) {
        String cleanQuery = (query != null && !query.trim().isEmpty()) ? query.trim() : null;
        String cleanType = (travelType != null && !travelType.trim().equalsIgnoreCase("all") && !travelType.trim().isEmpty()) ? travelType.trim() : null;
        return destinationRepository.searchDestinations(cleanQuery, cleanType, maxCost);
    }

    @PostConstruct
    public void seedInitialDestinationsIfEmpty() {
        if (destinationRepository.count() == 0) {
            List<Destination> list = new ArrayList<>();

            list.add(new Destination(
                    "Goa",
                    "Goa",
                    "Famous for pristine sandy beaches, vibrant coastal nightlife, Portuguese colonial architecture, and fresh seafood shacks.",
                    "November to February",
                    3500.0,
                    "Beach",
                    "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
                    4.9,
                    "Baga Beach, Calangute, Aguada Fort, Basilica of Bom Jesus, Dudhsagar Falls"
            ));

            list.add(new Destination(
                    "Manali",
                    "Himachal Pradesh",
                    "A high-altitude Himalayan resort town known for snow-capped peaks, pine forests, adventure sports, and scenic mountain valleys.",
                    "October to June",
                    3000.0,
                    "Hill Station",
                    "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=800&q=80",
                    4.8,
                    "Solang Valley, Rohtang Pass, Hadimba Temple, Old Manali, Jogini Waterfall"
            ));

            list.add(new Destination(
                    "Jaipur",
                    "Rajasthan",
                    "The majestic Pink City renowned for magnificent royal forts, opulent palaces, vibrant bazaars, and rich Rajasthani heritage.",
                    "October to March",
                    2800.0,
                    "Heritage",
                    "https://images.unsplash.com/photo-1603262110263-fb010d6e59d4?auto=format&fit=crop&w=800&q=80",
                    4.7,
                    "Hawa Mahal, Amber Palace, City Palace, Jantar Mantar, Nahargarh Fort"
            ));

            list.add(new Destination(
                    "Kerala (Alleppey)",
                    "Kerala",
                    "God's Own Country famous for tranquil backwaters, traditional houseboats, Ayurvedic wellness retreats, and lush palm groves.",
                    "September to March",
                    3800.0,
                    "Nature",
                    "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=800&q=80",
                    4.9,
                    "Alleppey Backwaters, Marari Beach, Vembanad Lake, Krishnapuram Palace"
            ));

            list.add(new Destination(
                    "Hyderabad",
                    "Telangana",
                    "The historic City of Pearls blending legendary Nizami architecture, bustling markets, world-famous biryani, and high-tech elegance.",
                    "October to February",
                    2500.0,
                    "Heritage",
                    "https://images.unsplash.com/photo-1572455044327-7348c1be7267?auto=format&fit=crop&w=800&q=80",
                    4.6,
                    "Charminar, Golconda Fort, Ramoji Film City, Hussain Sagar, Chowmahalla Palace"
            ));

            list.add(new Destination(
                    "Ooty",
                    "Tamil Nadu",
                    "The Queen of Hill Stations in the Nilgiri hills, featuring sprawling tea gardens, cool mist, pine forests, and heritage toy train rides.",
                    "March to June & September to November",
                    2600.0,
                    "Hill Station",
                    "https://images.unsplash.com/photo-1589182373726-e4f658ab50f0?auto=format&fit=crop&w=800&q=80",
                    4.6,
                    "Ooty Lake, Botanical Gardens, Doddabetta Peak, Pykara Falls, Nilgiri Mountain Railway"
            ));

            list.add(new Destination(
                    "Varanasi",
                    "Uttar Pradesh",
                    "One of the world's oldest continually inhabited cities, sacred Ghats along the holy River Ganga, mesmerizing Aarti, and timeless spirituality.",
                    "October to March",
                    2200.0,
                    "Spiritual",
                    "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=800&q=80",
                    4.8,
                    "Dashashwamedh Ghat, Kashi Vishwanath Temple, Assi Ghat, Sarnath, Manikarnika Ghat"
            ));

            list.add(new Destination(
                    "Rishikesh",
                    "Uttarakhand",
                    "The Yoga Capital of the World along the foothills of the Himalayas, famed for white water river rafting, cliff jumping, and serene ashrams.",
                    "September to April",
                    2400.0,
                    "Adventure",
                    "https://images.unsplash.com/photo-1544735716-392fe2489ffa?auto=format&fit=crop&w=800&q=80",
                    4.7,
                    "Laxman Jhula, Triveni Ghat, Shivpuri Rafting, Beatles Ashram, Neer Garh Waterfall"
            ));

            list.add(new Destination(
                    "Agra",
                    "Uttar Pradesh",
                    "Home to the timeless wonder of the world—the Taj Mahal—along with stunning Mughal architecture and exquisite handicrafts.",
                    "October to March",
                    2700.0,
                    "Heritage",
                    "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=800&q=80",
                    4.9,
                    "Taj Mahal, Agra Fort, Mehtab Bagh, Fatehpur Sikri, Itimad-ud-Daulah"
            ));

            list.add(new Destination(
                    "Udaipur",
                    "Rajasthan",
                    "The romantic City of Lakes crowned with sparkling marble palaces, sunset boat cruises, and exquisite royal courtyards.",
                    "September to March",
                    3200.0,
                    "Heritage",
                    "https://images.unsplash.com/photo-1615836245337-f5b9b2303f10?auto=format&fit=crop&w=800&q=80",
                    4.8,
                    "City Palace, Lake Pichola, Jag Mandir, Saheliyon-ki-Bari, Fateh Sagar Lake"
            ));

            destinationRepository.saveAll(list);
        }
    }
}

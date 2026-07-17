import Foundation

struct SDUICard: Decodable {
    let id: String
    let label: String?
    let children: [SDUIComponent]
}

import Foundation

struct SDUIScreenResponse: Decodable {
    let screen: SDUIScreen
}

struct SDUIScreen: Decodable {
    let id: String
    let title: String
    let components: [SDUIComponent]
}

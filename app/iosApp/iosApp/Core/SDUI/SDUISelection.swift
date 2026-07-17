import Foundation

struct SDUISelection: Decodable { 
    let id: String
    let label: String
    let options: [SDUIOption] 
}
